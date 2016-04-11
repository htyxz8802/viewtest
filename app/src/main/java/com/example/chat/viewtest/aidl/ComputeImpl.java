package com.example.chat.viewtest.aidl;

import android.os.RemoteException;

import com.example.chat.viewtest.ICompute;

/**
 * Created by htyxz on 2016/3/22.
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
