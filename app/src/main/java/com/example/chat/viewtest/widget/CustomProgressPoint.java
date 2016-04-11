package com.example.chat.viewtest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.chat.viewtest.R;

/**
 * Created by htyxz on 2016/4/9.
 */
public class CustomProgressPoint extends ImageView {
    private int pointCount ;
    private int pointSize ;
    private int pointColor ;
    private int pointBackColor ;
    private int mSpliteSize = 10 ;  //间隔长度
    private int mCurrent = 1 ;

    private int time = 1000 ;

    private boolean isOver = false ;


    private Paint mPaint ;

    public CustomProgressPoint(Context context) {
        super(context);
    }

    public CustomProgressPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomProgressPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public CustomProgressPoint(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressPoint,defStyleAttr,0);
        int a = ta.getIndexCount() ;
        for (int i = 0 ; i < a ; i++){
            int attr = ta.getIndex(i) ;
            switch (attr){
                case R.styleable.CustomProgressPoint_pointCount:
                    pointCount = ta.getInt(attr,6) ;
                    break;
                case R.styleable.CustomProgressPoint_pointSize :
                    pointSize = ta.getInt(attr, 20);
                    break;
                case R.styleable.CustomProgressPoint_pointColor:
                    pointColor = ta.getColor(attr, Color.WHITE) ;
                    break;
                case R.styleable.CustomProgressPoint_pointBackColor :
                    pointBackColor = ta.getColor(attr,Color.BLACK) ;
                    break;
                case R.styleable.CustomProgressPoint_time:
                    time = ta.getInt(attr,1000) ;
                    break;
            }
        }

        mPaint = new Paint() ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isOver){
                    if (mCurrent > 6){
                        mCurrent = 1 ;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                    mCurrent++ ;
                }
            }
        }).start();
    }


    public void Over(){
        isOver = true ;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int left = getPaddingLeft()  ;
        int top = getPaddingTop() ;

        mPaint.setColor(pointColor);
        mPaint.setStyle(Paint.Style.FILL);

        for (int i=1 ;i <= pointCount ;i++){
            float x =left + i * (mSpliteSize + pointSize * 2) ;
            float y = top + pointSize ;
            if (i == mCurrent){
                mPaint.setColor(pointColor);
            }else{
                mPaint.setColor(pointBackColor);
            }
            canvas.drawCircle(x, y,pointSize,mPaint);
        }
    }
}
