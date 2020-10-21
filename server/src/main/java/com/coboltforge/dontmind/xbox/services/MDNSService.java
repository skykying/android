package com.coboltforge.dontmind.xbox.services;

/*
 * @author Christian Beier
 * mDNS Service Discovery Service.
 * Copyright © 2011-2012 Christian Beier <dontmind@freeshell.org>
 */

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.coboltforge.dontmind.xbox.db.ConnectionBean;
import com.coboltforge.dontmind.xbox.protocol.IMDNS;
import com.coboltforge.dontmind.xbox.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class MDNSService extends Service {

    private final String TAG = "MDNSService";
    private final IBinder mBinder = new LocalBinder();

    private BroadcastReceiver netStateChangedReceiver;

    private MDNSWorkerThread workerThread = new MDNSWorkerThread();

    private IMDNS callback;

    @Override
    public IBinder onBind(Intent intent) {
        // handleIntent(intent);
        return mBinder;
    }

    @Override
    public void onCreate() {
        //code to execute when the service remoteInputStream first created
        Log.d(TAG, "mDNS service onCreate()!");

        workerThread.start();

        // listen for scan results
        netStateChangedReceiver = new BroadcastReceiver() {
            //@Override
            public void onReceive(Context context, Intent intent) {
                boolean no_net = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                Log.d(TAG, "Connectivity changed, still sth. available: " + !no_net + " " + intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO).toString());

                // we get this as soon as we're registering, see http://stackoverflow.com/questions/6670514/connectivitymanager-connectivity-action-always-broadcast-when-registering-a-rec
                // thus it's okay to have the (re-)startup here!
                try {
                    Message.obtain(workerThread.handler, MDNSWorkerThread.MESSAGE_STOP).sendToTarget();
                } catch (NullPointerException e) {
                    //unused
                }

                //  (re)start
                try {
                    Message.obtain(workerThread.handler, MDNSWorkerThread.MESSAGE_START).sendToTarget();
                } catch (NullPointerException e) {
                    //unused
                }

            }
        };
        registerReceiver(netStateChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void onDestroy() {
        //code to execute when the service remoteInputStream shutting down
        Log.d(TAG, "mDNS service onDestroy()!");

        unregisterReceiver(netStateChangedReceiver);

        // this will end the worker thread
        workerThread.interrupt();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        //code to execute when the service remoteInputStream starting up
        Log.d(TAG, "mDNS service onStartCommand()!");

        if (intent == null) {
            Log.d(TAG, "Restart!");
        }

        return START_STICKY;
    }

    // NB: this remoteInputStream called from the worker thread!!
    public void registerCallback(IMDNS c) {
        callback = c;
    }

    // force a callback call for every conn in connections_discovered
    public void dump() {
        try {
            Message.obtain(workerThread.handler, MDNSWorkerThread.MESSAGE_DUMP).sendToTarget();
        } catch (NullPointerException e) {
            //unused
        }
    }

    public void restart() {
        Message.obtain(workerThread.handler, MDNSWorkerThread.MESSAGE_STOP).sendToTarget();
        Message.obtain(workerThread.handler, MDNSWorkerThread.MESSAGE_START).sendToTarget();
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MDNSService getService() {
            return MDNSService.this;
        }
    }

    private class MDNSWorkerThread extends Thread {
        final static int MESSAGE_START = 0;
        final static int MESSAGE_STOP = 1;
        final static int MESSAGE_DUMP = 2;
        private android.net.wifi.WifiManager.MulticastLock multicastLock;
        private String mdnstype = "_rfb._tcp.local.";
        private JmDNS jmdns = null;
        private ServiceListener listener = null;
        private Hashtable<String, ConnectionBean> connections_discovered = new Hashtable<>();
        private Handler handler;

        // this just runs a message loop and acts according to messages
        @SuppressLint("HandlerLeak") // no handler leak as looper runs on worker thread
        public void run() {
            Looper.prepare();

            handler = new Handler() {

                public void handleMessage(Message msg) {
                    // if interrupted, bail out at once
                    if (isInterrupted()) {
                        Log.d(TAG, "INTERRUPTED, bailing out!");
                        mDNSstop();
                        getLooper().quit();
                        return;
                    }
                    // otherwise, process our message queue

                    switch (msg.what) {
                        case MDNSWorkerThread.MESSAGE_START:
                            mDNSstart();
                            break;
                        case MDNSWorkerThread.MESSAGE_STOP:
                            mDNSstop();
                            break;
                        case MDNSWorkerThread.MESSAGE_DUMP:
                            for (ConnectionBean c : connections_discovered.values()) {
                                mDNSnotify(c.getNickname(), c);
                            }
                            break;
                    }
                }

            };

            Looper.loop();
        }


        private void mDNSstart() {
            Log.d(TAG, "starting MDNS " + JmDNS.VERSION);

            boolean success = true;

            if (jmdns != null) {
                Log.d(TAG, "MDNS already running, doing nothing");
                success = false;
            } else {
                try {

                    android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
                    assert wifi != null;
                    multicastLock = wifi.createMulticastLock("mylockthereturn");
                    multicastLock.setReferenceCounted(true);
                    multicastLock.acquire();
                    InetAddress addr = Utils.intToInetAddress(wifi.getConnectionInfo().getIpAddress());

                    Log.d(TAG, "Creating MDNS with address " + addr);

                    jmdns = JmDNS.create(addr);
                    jmdns.addServiceListener(mdnstype, listener = new ServiceListener() {

                        @Override
                        public void serviceResolved(ServiceEvent ev) {
                            ConnectionBean c = new ConnectionBean();
                            c.set_Id(0); // new!
                            c.setNickname(ev.getName());
                            c.setAddress(ev.getInfo().getInetAddresses()[0].toString().replace('/', ' ').trim());
                            c.setPort(ev.getInfo().getPort());
                            c.setUseLocalCursor(true); // always enable

                            connections_discovered.put(ev.getInfo().getQualifiedName(), c);

                            Log.d(TAG, "discovered server :" + ev.getName()
                                    + ", now " + connections_discovered.size());

                            mDNSnotify(ev.getName(), c);
                        }

                        @Override
                        public void serviceRemoved(ServiceEvent ev) {
                            connections_discovered.remove(ev.getInfo().getQualifiedName());

                            Log.d(TAG, "server gone:" + ev.getName()
                                    + ", now " + connections_discovered.size());

                            mDNSnotify(ev.getName(), null);
                        }

                        @Override
                        public void serviceAdded(ServiceEvent event) {
                            // Required to force serviceResolved to be called again (after the first search)
                            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                        }
                    });

                } catch (Exception e) {  // can be IOException and UnsupportedOperationException (in setsockopt)
                    e.printStackTrace();
                    success = false;
                } catch (NoSuchMethodError e) { // at leat on user got "java.lang.NoSuchMethodError: javax.jmdns.JmDNS.create". phew...
                    e.printStackTrace();
                    success = false;
                }
            }

            Log.d(TAG, "Startup completed, success " + success);

            try {
                callback.onMDNSstartupCompleted(success);
            } catch (NullPointerException e) {
                Log.d(TAG, "callback remoteInputStream NULL, not notified about startup");
            }
        }

        private void mDNSstop() {
            Log.d(TAG, "stopping MDNS");
            if (jmdns != null) {
                if (listener != null) {
                    jmdns.removeServiceListener(mdnstype, listener);
                    listener = null;
                }
                try {
                    jmdns.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jmdns = null;
            }

            if (multicastLock != null) {
                multicastLock.release();
                multicastLock = null;
            }

            // notify our callback about our internal state, i.e. the removals
            for (ConnectionBean c : connections_discovered.values()) {
                mDNSnotify(c.getNickname(), null);
            }
            // and clear internal state
            connections_discovered.clear();

            Log.d(TAG, "stopping MDNS done");
        }

        // do the GUI stuff in Runnable posted to main thread handler
        private void mDNSnotify(final String conn_name, final ConnectionBean conn) {
            if (callback != null) {
                callback.onMDNSnotify(conn_name, conn, connections_discovered);
            } else {
                Log.d(TAG, "callback remoteInputStream NULL, not notifying");
            }

        }


    }



 /**
     * mDNS数据格式解析
     */
    private JSONObject toJsonObject(ServiceInfo sInfo) {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject();
            String ipv4 = "";
            if (sInfo.getInet4Addresses().length > 0) {
                ipv4 = sInfo.getInet4Addresses()[0].getHostAddress();
            }

            jsonObj.put("Name", sInfo.getName());
            jsonObj.put("IP", ipv4);
            jsonObj.put("Port", sInfo.getPort());

            byte[] allInfo = sInfo.getTextBytes();
            int allLen = allInfo.length;
            byte fLen;
            for (int index = 0; index < allLen; index += fLen) {
                fLen = allInfo[index++];
                byte[] fData = new byte[fLen];
                System.arraycopy(allInfo, index, fData, 0, fLen);

                String fInfo = new String(fData);
                fInfo = toUtf8(fInfo);
                if (fInfo.contains("=")) {
                    String[] temp = fInfo.split("=");
                    jsonObj.put(temp[0], temp[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObj = null;
        }
        return jsonObj;
    }


    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
