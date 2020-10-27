package me.jessyan.mvparms.demo.usbserial;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialService extends Service implements SerialInputOutputManager.Listener {
    private final String TAG = SerialService.class.getSimpleName();

    private static UsbSerialPort sPort = null;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private final Random generator = new Random();
    private SerialInputOutputManager mSerialIoManager;

    private UsbCallback usbCallback;
    public SerialService() {
    }


    public interface UsbCallback{
        public void onNewUsbData(byte[] data);
    }

    public class UBinder extends Binder {

        public SerialService getService(){
            return SerialService.this;
        }


    }
    public void regsiterListener(UsbCallback _usbCallback){
        usbCallback = _usbCallback;
    }

    //getRandomNumber是Service暴露出去供client调用的公共方法
    public int getRandomNumber(){
        return generator.nextInt();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new UBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUsbManager();
        closeUsbPort(sPort);
        usbCallback = null;
    }

    @Override
    public void onNewData(byte[] data) {
        usbCallback.onNewUsbData(data);
    }

    public void usbInit(UsbSerialPort usbSerialPort){
        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        UsbDeviceConnection connection = usbManager.openDevice(usbSerialPort.getDriver().getDevice());
        if (connection == null) {
            return;
        }

        try {
            usbSerialPort.open(connection);
            usbSerialPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            sPort = usbSerialPort;
        } catch (IOException e) {
            Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
            try {
                usbSerialPort.close();
            } catch (IOException e2) {
            }

            return;
        }
    }

    public void stopUsbManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    public void closeUsbPort(UsbSerialPort usbSerialPort) {
        if (usbSerialPort != null) {
            try {
                usbSerialPort.close();
            } catch (IOException e) {
                // Ignore.
            }
        }
    }

    public void startUsbManager(UsbSerialPort usbSerialPort) {
        if (usbSerialPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(usbSerialPort, this);
            mExecutor.submit(mSerialIoManager);
        }
    }

    @Override
    public void onRunError(Exception e) {

    }
}
