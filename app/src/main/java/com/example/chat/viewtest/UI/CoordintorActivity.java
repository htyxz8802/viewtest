package com.example.chat.viewtest.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.chat.viewtest.R;

public class CoordintorActivity extends Activity {

    private Toolbar toolbar ;
    private FloatingActionButton bt_FlowLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coordintor);
        setContentView(R.layout.coordinator);
        bt_FlowLayout = (FloatingActionButton) findViewById(R.id.bt_FlowLayout);


        toolbar = (Toolbar) findViewById(R.id.test_toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.black));
//        toolbar.setTitle("toolbar");
    }
}
