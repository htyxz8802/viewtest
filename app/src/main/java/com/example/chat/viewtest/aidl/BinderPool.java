package com.example.chat.viewtest.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.chat.viewtest.IBinderPool;
import com.example.chat.viewtest.MyServices.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by htyxz on 2016/3/22.
 */
public class BinderPool {

    private static final String tag = "BinderPool";

    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context mContext ;
    private IBinderPool mBinderPool ;
    private static volatile BinderPool sInstance ;

    private CountDownLatch mConnectBinderPoolCountDownLatch ;

    private BinderPool(Context context){
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getsInstance(Context context){
            if (null == sInstance){
                synchronized (BinderPool.class){
                    if (null == sInstance){
                        sInstance = new BinderPool(context);
                    }
                }
            }
        return sInstance;
    }

    private synchronized void connectBinderPoolService(){
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);

        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if (null != mBinderPool){
               binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);

            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool = null ;
            connectBinderPoolService();
        }
    };


    public static class BinderPoolImpl extends IBinderPool.Stub{

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {

            IBinder binder = null ;
            switch (binderCode){
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityConterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }

            return binder;
        }
    }

}
