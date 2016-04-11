package com.example.chat.viewtest.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chat.viewtest.IBookManager;
import com.example.chat.viewtest.R;
import com.example.chat.viewtest.aidl.Book;
import com.example.chat.viewtest.aidl.BookManagerService;
import com.example.chat.viewtest.aidl.IOnNewBookArrivedListener;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String tag = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 0x00;

 private IBookManager mRemoteBookManager ;



    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(tag,"receive new book:"+((Book)msg.obj).getName());
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);

            mRemoteBookManager = bookManager ;

            try {
                List<Book> list = bookManager.getBookList() ;
                Log.i(tag, "query book list , list type:" + list.getClass());

                Log.i(tag, "query book list size:" + list.size());
                bookManager.addBook(new Book(3, "mybook"));

                List<Book> newList = bookManager.getBookList() ;
                Log.i(tag,"query book list , list type:" + newList.getClass());

                Log.i(tag, "query book list size:" + newList.size());
                bookManager.registerListener(onNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null ;
            Log.i(tag, "binder died");
        }
    };


    private IOnNewBookArrivedListener onNewBookArrivedListener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void OnNewBookkArrived(Book book) throws RemoteException {
            myHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,book).sendToTarget();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent bookService = new Intent(this, BookManagerService.class);
        bindService(bookService,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (null != mRemoteBookManager && mRemoteBookManager.asBinder().isBinderAlive()){
            try {
                Log.i(tag, "unregister listener:"+onNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(onNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        super.onDestroy();
    }
}
