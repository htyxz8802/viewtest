// IBookManager.aidl
package com.example.chat.viewtest;

import com.example.chat.viewtest.aidl.Book;
import com.example.chat.viewtest.aidl.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}
