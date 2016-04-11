package com.example.chat.viewtest.aidl;

import android.os.RemoteException;

import com.example.chat.viewtest.ISecurityCenter;

/**
 * Created by htyxz on 2016/3/22.
 */
public class SecurityConterImpl extends ISecurityCenter.Stub {

    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i=0;i<chars.length; i++){
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrype(String content) throws RemoteException {
        return encrypt(content);
    }
}
