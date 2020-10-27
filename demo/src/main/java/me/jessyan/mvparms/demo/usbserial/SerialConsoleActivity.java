package me.jessyan.mvparms.demo.usbserial;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.setting.DeviceSetting;
import me.jessyan.mvparms.demo.ui.BaseToolbarActivity;
import me.jessyan.mvparms.demo.usbserial.util.HexDump;

public class SerialConsoleActivity extends BaseToolbarActivity  implements  SerialService.UsbCallback{

    private final String TAG = SerialConsoleActivity.class.getSimpleName();

    private static UsbSerialPort sPort = null;

    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;
    private CheckBox chkDTR;
    private CheckBox chkRTS;


    private SerialService service = null;

    private boolean isBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_console);
        mTitleTextView = (TextView) findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) findViewById(R.id.consoleText);
        mScrollView = (ScrollView) findViewById(R.id.demoScroller);
        chkDTR = (CheckBox) findViewById(R.id.checkBoxDTR);
        chkRTS = (CheckBox) findViewById(R.id.checkBoxRTS);

        chkDTR.setOnCheckedChangeListener((buttonView, isChecked) -> {
                try {
                    sPort.setDTR(isChecked);
                }catch (IOException x){}
        });
        chkDTR.setVisibility(View.GONE);

        chkRTS.setOnCheckedChangeListener((buttonView, isChecked) -> {
                try {
                    sPort.setRTS(isChecked);
                }catch (IOException x){
                }
        });
        chkRTS.setVisibility(View.GONE);

        Intent intent = new Intent(this, SerialService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);


    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBound = true;
            SerialService.UBinder myBinder = (SerialService.UBinder)binder;
            service = myBinder.getService();

            Log.i("DemoLog", "ActivityA onServiceConnected");
            int num = service.getRandomNumber();
            Log.i("DemoLog", "ActivityA 中调用 TestService的getRandomNumber方法, 结果: " + num);


            service.usbInit(sPort);
            registerCallback(service);
            service.startUsbManager(sPort);
            mTitleTextView.setText("Service start ok !");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.i("DemoLog", "ActivityA onServiceDisconnected");
            mTitleTextView.setText("Service disconnected !");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void registerCallback(SerialService serialService){
        serialService.regsiterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, port=" + sPort);
    }


    private void updateReceivedData(byte[] data) {

        String xdata =  HexDump.toHexString(data);
        xdata = new String(data);

        if(mDumpTextView.getText().length() > 100){
            mDumpTextView.clearComposingText();
            mDumpTextView.setText("");
        }
        mDumpTextView.append(xdata);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }

    /**
     * Starts the activity, using the supplied driver instance.
     *
     * @param context
     * @param port
     */
    static void show(Context context, UsbSerialPort port) {
        sPort = port;
        final Intent intent = new Intent(context, SerialConsoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    @Override
    public void onNewUsbData(byte[] data) {

        try {
            sPort.write(data,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        DeviceSetting deviceSettingObject = new DeviceSetting();
        deviceSettingObject.setAddress(new String(data).trim());

        String stringSettings = gson.toJson(deviceSettingObject);
        Log.d(TAG, "deviceSetting = " + stringSettings);
        runOnUiThread(()->{
            updateReceivedData(stringSettings.getBytes());
        });

        DeviceSetting deviceSetting = gson.fromJson(stringSettings, DeviceSetting.class);
        Log.d(TAG, "deviceSetting, address=" + deviceSetting.getAddress());
    }
}
