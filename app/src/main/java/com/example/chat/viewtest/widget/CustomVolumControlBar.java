package com.example.chat.viewtest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.chat.viewtest.R;

/**
 * Created by htyxz on 2016/3/15.
 */
public class CustomVolumControlBar extends View {

    private int mFristColor;
    private int mSecondColor ;
    private int mCircleWidth ;
    private int dontCount;
    private int mSpitSize ;

    private int mCurrentCount = 3 ;

    private Bitmap mImage ;

    private Paint mPaint ;
    private Rect mRect ;

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar,defStyleAttr,0);
        int a = ta.getIndexCount();
        for (int i=0 ;i < a ;i++){
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.CustomVolumControlBar_firstColor :
                    mFristColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomVolumControlBar_secondColor :
                    mSecondColor = ta.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.CustomVolumControlBar_circleWidth:
                    mCircleWidth = ta.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_dotCount:
                    dontCount = ta.getInt(attr,20);
                    break;
                case R.styleable.CustomVolumControlBar_splitSize:
                    mSpitSize = ta.getInt(attr,20);
                    break;
                case R.styleable.CustomVolumControlBar_bg :
                    mImage = BitmapFactory.decodeResource(getResources(),ta.getResourceId(attr,0));
                    break;
            }
        }

        ta.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true); //消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); //设置圆环
        mPaint.setStrokeCap(Paint.Cap.ROUND); //定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); //设置空心

        int center = getWidth() / 2 ;
        int radius = center - mCircleWidth/2 ;

        /*
            画块
         */
        drawOval(canvas,center,radius);

        /**
         * 计算内切正方形的位置
         */
        int relRadius = radius - mCircleWidth / 2;

        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f /2 *relRadius)+ mCircleWidth;
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f /2 *relRadius)+ mCircleWidth;

        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius) ;
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius) ;

        if (mImage.getWidth() < Math.sqrt(2) * relRadius){
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
            mRect.right = (int) (mRect.left + mImage.getWidth());
            mRect.bottom = (int) (mRect.top + mImage.getHeight());
        }
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }

    private int xDown,xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown){
                    down();
                }else{
                    up();
                }
                break;

        }

        return true;
    }

    void up(){
        mCurrentCount++;
        postInvalidate();
    }
    void down(){
        mCurrentCount--;
        postInvalidate();
    }

    private void drawOval(Canvas canvas,int center,int radius){
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize  = (360 * 1.0f - dontCount * mSpitSize)/dontCount ;
        RectF mRect = new RectF(center - radius,center - radius , center + radius , center + radius);

        mPaint.setColor(mFristColor);
        for(int i= 0 ; i< dontCount ; i++){
            canvas.drawArc(mRect,i * (itemSize + mSpitSize),itemSize,false,mPaint);
        }

        mPaint.setColor(mSecondColor);
        for (int j =0 ; j< mCurrentCount; j++){
            canvas.drawArc(mRect,j * (itemSize + mSpitSize),itemSize,false,mPaint);
        }




    }
}
