package com.example.chat.viewtest.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chat.viewtest.MyServices.MessengerService;
import com.example.chat.viewtest.MyServices.MyConstants;
import com.example.chat.viewtest.R;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    private Messenger mService ;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello , this is client.");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger ;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent service = new Intent(this, MessengerService.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {

        unbindService(mConnection);
        super.onDestroy();
    }


    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants.MSG_FROM_SERVICES:
                    Log.i(TAG,"receive msg from Services: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

}
