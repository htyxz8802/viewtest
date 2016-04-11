package com.example.chat.viewtest.UI;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.chat.viewtest.ICompute;
import com.example.chat.viewtest.ISecurityCenter;
import com.example.chat.viewtest.R;
import com.example.chat.viewtest.aidl.BinderPool;
import com.example.chat.viewtest.aidl.ComputeImpl;
import com.example.chat.viewtest.aidl.SecurityConterImpl;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String tag = "BinderPoolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork(){
        BinderPool binderPool = BinderPool.getsInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);

        ISecurityCenter mIsecurityCenter = SecurityConterImpl.asInterface(securityBinder);

        Log.d(tag,"visit ISecurityCenter");
        String msg = "hello world 安卓";

        try {
            String passwd = mIsecurityCenter.encrypt(msg);
            Log.d(tag,"encrypt: " + passwd);
            Log.d(tag,"decrypt: " + mIsecurityCenter.decrype(passwd));

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        Log.d(tag,"visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute mCompute = ComputeImpl.asInterface(computeBinder);

        try {
            Log.d(tag,"decrypt: " + mCompute.add(3,5));

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
