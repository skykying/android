package com.coboltforge.dontmind.xbox.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.coboltforge.dontmind.xbox.R;
import com.coboltforge.dontmind.xbox.color.COLORMODEL;
import com.coboltforge.dontmind.xbox.common.Constants;
import com.coboltforge.dontmind.xbox.db.ConnectionBean;
import com.coboltforge.dontmind.xbox.db.ConnectionContext;
import com.coboltforge.dontmind.xbox.db.DatabaseProvider;
import com.coboltforge.dontmind.xbox.db.MostRecentBean;
import com.coboltforge.dontmind.xbox.db.VncDatabase;
import com.coboltforge.dontmind.xbox.protocol.IMDNS;
import com.coboltforge.dontmind.xbox.services.MDNSService;
import com.coboltforge.dontmind.xbox.ui.activity.AboutActivity;
import com.coboltforge.dontmind.xbox.ui.activity.EditBookmarkActivity;
import com.coboltforge.dontmind.xbox.ui.activity.HelpActivity;
import com.coboltforge.dontmind.xbox.ui.activity.ImportExportActivity;
import com.coboltforge.dontmind.xbox.ui.activity.VMBaseActivity;
import com.coboltforge.dontmind.xbox.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;


public class DnsActivity extends VMBaseActivity implements IMDNS, LifecycleObserver {

    private static final String TAG = DnsActivity.class.getSimpleName();

    private LinearLayout serverlist;
    private DatabaseProvider database;

    // service discovery stuff
    private MDNSService boundMDNSService;
    private ServiceConnection connection_MDNSService;
    private android.os.Handler handler = new android.os.Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vm_main_activity);

        Intent intent = getIntent();
        String str = intent.getStringExtra("data");

        // get package debug flag and set it
        Utils.DEBUG(this);

        // start the MDNS service
        startMDNSService();

        // and (re-)bind to MDNS service
        bindToMDNSService(new Intent(this, MDNSService.class));

        serverlist = (LinearLayout) findViewById(R.id.discovered_servers_list);

        database = new DatabaseProvider();
        final SharedPreferences settings = getSharedPreferences(Constants.PREFSNAME, MODE_PRIVATE);
    }


    protected void onDestroy() {

        Log.d(TAG, "onDestroy()");

        super.onDestroy();

        database.close();

        unbindFromMDNSService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(Intent.ACTION_VIEW, null, this, MDNSService.class));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dns_menu, menu);

        menu.findItem(R.id.itemMDNSRestart).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemMDNSRestart) {
            serverlist.removeAllViews();
            findViewById(R.id.discovered_servers_waitwheel).setVisibility(View.VISIBLE);

            try {
                boundMDNSService.restart();
            } catch (NullPointerException e) {
            }
        }

        return true;
    }

    DatabaseProvider getDatabaseHelper() {
        return database;
    }


    void startMDNSService() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                Intent serviceIntent = new Intent(Intent.ACTION_VIEW, null, this, MDNSService.class);
                this.startService(serviceIntent);
            } else {
                ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
            }
        } else {
            Intent serviceIntent = new Intent(Intent.ACTION_VIEW, null, this, MDNSService.class);
            this.startService(serviceIntent);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onEnterForeground() {
        startMDNSService();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    void bindToMDNSService(Intent serviceIntent) {
        unbindFromMDNSService();
        connection_MDNSService = new MDNSServiceConnection();
        if (!this.bindService(serviceIntent, connection_MDNSService, Context.BIND_AUTO_CREATE)) {
            Toast.makeText(this, "Could not bind to MDNSService!", Toast.LENGTH_LONG).show();
        }
    }

    void unbindFromMDNSService() {
        if (connection_MDNSService != null) {
            this.unbindService(connection_MDNSService);
            connection_MDNSService = null;
            try {
                boundMDNSService.registerCallback(null);
                boundMDNSService = null;
            } catch (NullPointerException e) {
                // was already null
            }
        }
    }

    @Override
    public void onMDNSnotify(final String conn_name, final ConnectionBean conn, final Hashtable<String, ConnectionBean> connections_discovered) {
        handler.postDelayed(new Runnable() {
            public void run() {

                String msg;
                if (conn != null) {
                    msg = getString(R.string.server_found) + ": " + conn_name;
                } else {
                    msg = getString(R.string.server_removed) + ": " + conn_name;
                }

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                // update
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                /*
                 * we don't care whether this remoteInputStream a conn add or removal (conn!=null or conn==null),
                 * we always rebuild the whole view for the sake of code simplicity
                 */
                serverlist.removeAllViews();
                // refill
                synchronized (connections_discovered) {
                    for (final ConnectionBean c : connections_discovered.values()) {
                        // the list item
                        View v = vi.inflate(R.layout.discovered_servers_list_item, null);

                        // name part of list item
                        TextView name = (TextView) v.findViewById(R.id.discovered_server_name);
                        name.setText(c.getNickname());
                        name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Starting discovered connection " + c.toString());
                                database.writeRecent(c);
                                Intent intent = new Intent(DnsActivity.this, VncCanvasActivity.class);
                                intent.putExtra(Constants.CONNECTION, c.Gen_getValues());
                                startActivity(intent);
                            }
                        });

                        serverlist.addView(v);
                    }
                }

            }
        }, 1);

    }

    @Override
    public void onMDNSstartupCompleted(boolean wasSuccessful) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.discovered_servers_waitwheel).setVisibility(View.GONE);
            }
        });
    }

public class MDNSServiceConnection implements ServiceConnection {
    public void onServiceConnected(ComponentName className, IBinder service) {
        // This remoteInputStream called when the connection with the service has been
        // established, giving us the service object we can use to
        // interact with the service. Because we have bound to a explicit
        // service that we know remoteInputStream running in our own process, we can
        // cast its IBinder to a concrete class and directly access it.
        boundMDNSService = ((MDNSService.LocalBinder) service).getService();

        // register our callback to be notified about changes
        boundMDNSService.registerCallback(DnsActivity.this);

        // and force a dump of discovered connections
        boundMDNSService.dump();

        Log.d(TAG, "connected to MDNSService " + boundMDNSService);
    }

    public void onServiceDisconnected(ComponentName className) {

        Log.d(TAG, "disconnected from MDNSService " + boundMDNSService);

        // This remoteInputStream called when the connection with the service has been
        // unexpectedly disconnected -- that remoteInputStream, its process crashed.
        // Because it remoteInputStream running in our same process, we should never
        // see this happen.
        boundMDNSService = null;
    }
}
}
