package com.example.chat.viewtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.chat.viewtest.UI.AnimatorActivity;
import com.example.chat.viewtest.UI.BinderPoolActivity;
import com.example.chat.viewtest.UI.BookManagerActivity;
import com.example.chat.viewtest.UI.CoordintorActivity;
import com.example.chat.viewtest.UI.MessengerActivity;
import com.example.chat.viewtest.UI.ScrollActivity;

public class MainActivity extends AppCompatActivity {

    private Button bt_Coordinator ;
    private Button bt_viewGroup ;
    private Button bt_viewGroupVerticalLinear ;
    private Button bt_FlowLayout ;
    private Button bt_zoomLayout ;
    private Button bt_showbigLayout ;
    private Button bt_messenger ;
    private Button bt_bookService ;
    private Button bt_ScrollView ;
    private Button bt_BinderPool ;
    private Button bt_animator ;
    private Button bt_point ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();


    }

    void init(){
        bt_Coordinator = (Button) findViewById(R.id.bt_Coordinator);
        bt_viewGroup = (Button) findViewById(R.id.bt_viewGroup);
        bt_viewGroupVerticalLinear = (Button) findViewById(R.id.bt_viewGroupVerticalLinear);
        bt_FlowLayout = (Button) findViewById(R.id.bt_FlowLayout);
        bt_zoomLayout = (Button) findViewById(R.id.bt_zoomLayout);
        bt_showbigLayout = (Button) findViewById(R.id.bt_showbigLayout);
        bt_messenger = (Button) findViewById(R.id.bt_messenger);
        bt_bookService = (Button) findViewById(R.id.bt_bookService);
        bt_ScrollView = (Button) findViewById(R.id.bt_ScrollView);
        bt_BinderPool = (Button) findViewById(R.id.bt_BinderPool);
        bt_animator = (Button) findViewById(R.id.bt_animator);
        bt_point = (Button) findViewById(R.id.bt_point);
    }

    void initView(){
        bt_Coordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CoordintorActivity.class);
                startActivity(intent);
            }
        });

        bt_viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewgroupActivity.class);
                startActivity(intent);
            }
        });

        bt_viewGroupVerticalLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewGroupVerticalLinear.class);
                startActivity(intent);
            }
        });

        bt_FlowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FlowActivity.class);
                startActivity(intent);
            }
        });
        bt_zoomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ZoomActivity.class);
                startActivity(intent);
            }
        });
        bt_showbigLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ShowBigActivity.class);
                startActivity(intent);
            }
        });
        bt_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MessengerActivity.class);
                startActivity(intent);
            }
        });
        bt_bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BookManagerActivity.class);
                startActivity(intent);
            }
        });
        bt_ScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ScrollActivity.class);
                startActivity(intent);
            }
        });
        bt_BinderPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BinderPoolActivity.class);
                startActivity(intent);
            }
        });
        bt_animator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AnimatorActivity.class);
                startActivity(intent);
            }
        });
        bt_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PonitActivity.class);
                startActivity(intent);
            }
        });

        new Thread(new testMhandler()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private class testMhandler implements  Runnable{
        @Override
        public void run() {

            Looper.prepare();

            Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Log.e("testMhandler","testMhandler mHandler" + System.currentTimeMillis());
                    super.handleMessage(msg);
                }
            };



            if (mHandler.getLooper() == Looper.getMainLooper()){
                Log.e("testMhandler", " mHandler is main" + System.currentTimeMillis());
            }else{
                Log.e("testMhandler"," mHandler is not main" + System.currentTimeMillis());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(0);


            Looper.loop();


            Looper.prepare();

            Handler mHandler1 = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Log.e("testMhandler","testMhandler mHandler1  " + System.currentTimeMillis());
                    super.handleMessage(msg);
                }
            };

            if (mHandler1.getLooper() == Looper.getMainLooper()){
                Log.e("testMhandler"," mHandler1111 is main" + System.currentTimeMillis());
            }else{
                Log.e("testMhandler"," mHandler111 is not main" + System.currentTimeMillis());
            }


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mHandler1.sendEmptyMessage(0);


            Looper.loop();
        }
    }
}
