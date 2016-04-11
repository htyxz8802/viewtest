// IOnNewBookArrivedListener.aidl
package com.example.chat.viewtest.aidl;

import com.example.chat.viewtest.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
   void OnNewBookkArrived(in Book book);
}
