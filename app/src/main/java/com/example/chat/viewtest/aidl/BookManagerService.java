package com.example.chat.viewtest.aidl;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.example.chat.viewtest.IBookManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String tag = "BookManagerService";
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (!mBookList.contains(book)) {
                mBookList.add(book);
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
           listeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    public BookManagerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        int check = checkCallingPermission("com.example.chat.viewtest.ACCESS_BOOK_SERVICE");
//        if (check == PackageManager.PERMISSION_DENIED){
//            Log.i(tag,"permission request failed");
//            return null ;
//        }
//        Log.i(tag,"permission request success");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(2,"ios"));

        new Thread(new ServieWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrvied(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = listeners.beginBroadcast();
        Log.i(tag,"N:" + N);
        for (int i = 0 ; i < N ; i++){
           IOnNewBookArrivedListener l = listeners.getBroadcastItem(i);
            if(null != l) {
                l.OnNewBookkArrived(book);
            }
        }
        listeners.finishBroadcast();
    }

    private class ServieWorker implements Runnable{

        @Override
        public void run() {

            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size()  +1 ;
                Book book = new Book(bookId,"new bookid#"+bookId);
                try {
                    onNewBookArrvied(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
