package com.example.chat.viewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chat.viewtest.widget.VerticalLinearLayout;

public class ViewGroupVerticalLinear extends AppCompatActivity {

    private VerticalLinearLayout verticalLinear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group_vertical_linear);
        init();
        initView() ;
    }


    void init(){
        verticalLinear = (VerticalLinearLayout)findViewById(R.id.id_main_ly);
    }
    void initView(){
        verticalLinear.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPagerChanger(int currentPage) {
                Toast.makeText(ViewGroupVerticalLinear.this, "第" + (currentPage + 1) + "页", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
