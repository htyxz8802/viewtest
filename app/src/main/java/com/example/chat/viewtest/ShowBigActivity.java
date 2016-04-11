package com.example.chat.viewtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ShowBigActivity extends AppCompatActivity {


    private ImageView iv_bg_pic ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big);
        init();
        initData() ;
    }

    void init(){
        iv_bg_pic = (ImageView) findViewById(R.id.iv_bg_pic);
    }

    void initView(){

    }

    void initData(){
        try {
            InputStream inputStream = getAssets().open("g.jpg");

            BitmapFactory.Options tmpOptions = new BitmapFactory.Options() ;
            tmpOptions.inJustDecodeBounds = true ;
            BitmapFactory.decodeStream(inputStream,null,tmpOptions) ;
            int width = tmpOptions.outWidth ;
            int height = tmpOptions.outHeight ;


            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(inputStream,false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565 ;
            Bitmap bitmap = regionDecoder.decodeRegion(new Rect(width/2-100,height / 2 -100,width /2 + 100, height /2 + 100),options) ;
            iv_bg_pic.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
