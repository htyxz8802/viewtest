package com.example.chat.viewtest.MyServices;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG,"receive msg from Client: " + msg.getData().getString("msg"));

                    Messenger client = msg.replyTo ;
                    Message replyMsg = Message.obtain(null,MyConstants.MSG_FROM_SERVICES);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","嗯，你的消息已经收到，稍后会回复你");
                    replyMsg.setData(bundle);

                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());


    public MessengerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication` channel to the service.
       return mMessenger.getBinder() ;
    }
}
