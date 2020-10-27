package me.jessyan.mvparms.demo.usbserial;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.setting.DeviceSetting;
import me.jessyan.mvparms.demo.ui.BaseToolbarActivity;
import me.jessyan.mvparms.demo.usbserial.util.HexDump;


public class DeviceConfigureActivity extends BaseToolbarActivity  implements  SerialService.UsbCallback{

    private final String TAG = DeviceConfigureActivity.class.getSimpleName();

    private static UsbSerialPort sPort = null;

    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;
    private CheckBox chkDTR;
    private CheckBox chkRTS;
    private Spinner modeSpinner;

    private Spinner prot1Spinner;
    private Spinner prot2Spinner;
    private Spinner prot3Spinner;
    private Spinner prot4Spinner;
    private Spinner prot5Spinner;
    private Spinner prot6Spinner;

    private SerialService service = null;

    private boolean isBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_configure);
        mTitleTextView = (TextView) findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) findViewById(R.id.consoleText);
        mScrollView = (ScrollView) findViewById(R.id.demoScroller);
        chkDTR = (CheckBox) findViewById(R.id.checkBoxDTR);
        chkRTS = (CheckBox) findViewById(R.id.checkBoxRTS);

        chkDTR.setOnCheckedChangeListener((v, isChecked)->{
                try {
                    sPort.setDTR(isChecked);
                }catch (IOException x){}
        });
        chkDTR.setVisibility(View.GONE);

        chkRTS.setOnCheckedChangeListener((v, isChecked)->{
                try {
                    sPort.setRTS(isChecked);
                }catch (IOException x){}
        });
        chkRTS.setVisibility(View.GONE);
        modeSpinner = this.findViewById(R.id.sport1);
        prot1Spinner = this.findViewById(R.id.sport1);
        prot2Spinner = this.findViewById(R.id.sport2);
        prot3Spinner = this.findViewById(R.id.sport3);
        prot4Spinner = this.findViewById(R.id.sport4);
        prot5Spinner = this.findViewById(R.id.sport5);
        prot6Spinner = this.findViewById(R.id.sport6);


        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            modeSpinner.setAdapter(adapter);
            modeSpinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot1Spinner.setAdapter(adapter);
            prot1Spinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot2Spinner.setAdapter(adapter);
            prot2Spinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot3Spinner.setAdapter(adapter);
            prot3Spinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot4Spinner.setAdapter(adapter);
            prot4Spinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot5Spinner.setAdapter(adapter);
            prot5Spinner.setSelection(1);
        }

        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prot6Spinner.setAdapter(adapter);
            prot6Spinner.setSelection(1);
        }

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


        if(mDumpTextView.getText().length() > 500){
            mDumpTextView.clearComposingText();
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
        final Intent intent = new Intent(context, DeviceConfigureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    @Override
    public void onNewUsbData(byte[] data) {
        runOnUiThread(()->{
            //updateReceivedData(data);
        });
        try {
            sPort.write(data,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        DeviceSetting deviceSettingObject = new DeviceSetting();
        deviceSettingObject.setAddress(new String(data).trim());

        String userJson = gson.toJson(deviceSettingObject);
        DeviceSetting deviceSetting = gson.fromJson(userJson, DeviceSetting.class);
        Log.d(TAG, "deviceSetting, address=" + deviceSetting.getAddress());
    }
}
