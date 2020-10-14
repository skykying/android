package me.jessyan.mvparms.demo.usbserial;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SerialService extends Service {
    public SerialService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
