package com.example.chat.viewtest.MyServices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.chat.viewtest.aidl.BinderPool;

public class BinderPoolService extends Service {

    private static final String tag = "BinderPoolService";

    private Binder mBinderPool = new BinderPool.BinderPoolImpl();


    public BinderPoolService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return mBinderPool;
    }
}
