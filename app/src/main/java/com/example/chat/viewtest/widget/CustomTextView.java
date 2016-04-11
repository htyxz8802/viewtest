package com.example.chat.viewtest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.chat.viewtest.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Created by htyxz on 2016/3/14.
 */
public class CustomTextView extends View {

    private static final int IMAGE_SCALE_FITXY = 0 ;
    private String titleText ;
    private int titleTextColor ;
    private int titleTextSize ;

    private Bitmap mImage ;
    private int mScaleType ;

    int width ;
    int height ;

    Paint paint ;
    Rect rect ;
    Rect mTextRect ;


    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView,defStyleAttr,0);
        int n = array.getIndexCount() ;
        for (int i=0 ; i < n ; i++){
            int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.CustomTitleView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr,0));
                    break;
                case R.styleable.CustomTitleView_imageScaleType:
                    mScaleType = array.getIndex(attr);
                    break;
                case R.styleable.CustomTitleView_titleText1:
                    titleText = array.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor1:
                    //默认设置为黑色
                    titleTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize1:

                    titleTextSize = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();

        paint = new Paint();
        paint.setTextSize(titleTextSize);

        rect = new Rect();
        mTextRect = new Rect();
        paint.getTextBounds(titleText,0,titleText.length(),mTextRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = RandomText();
                postInvalidate();
            }
        });

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) ;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY){ //设定指定值 或者MATCH_PARENT
            Log.e("CustomTextView","EXACTLY");
            width = widthSize ;
        }else {
           int desireImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth() ; //图片的宽度
           int desireText = getPaddingLeft() +  getPaddingRight() + mTextRect.width() ;

            if (widthMode == MeasureSpec.AT_MOST){//wrap_content
                int desire = Math.max(desireImg,desireText);
                width = Math.min(desire,widthSize);
                Log.e("CustomTextView","AT_MOST");
            }else{
                if (widthMode == MeasureSpec.UNSPECIFIED){
                    Log.e("CustomTextView","UNSPECIFIED");
                }
                width = Math.max(desireImg,desireText);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){ //设定指定值 或者MATCH_PARENT
            height = heightSize ;
        }else{
            int desire = getPaddingBottom() + getPaddingTop() + mImage.getHeight() + mTextRect.height();
            if (heightMode == MeasureSpec.AT_MOST){ //wrap_content
                height = Math.min(desire,heightSize);
            }else {
                height = desire;
            }
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //先画背景
        paint.setStrokeWidth(4); //设置边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.CYAN);

        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        //画文字
        rect.left = getPaddingLeft() ;
        rect.right = width - getPaddingRight() ;
        rect.top =getPaddingTop();
        rect.bottom = height - getPaddingBottom() ;

        paint.setColor(titleTextColor);
        paint.setStyle(Paint.Style.FILL);

        if (mTextRect.width() > width){
            TextPaint textPaint = new TextPaint(paint);
            String msg = TextUtils.ellipsize(titleText,textPaint,width- getPaddingLeft()-getPaddingRight(),TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),height - getPaddingBottom(),paint);
        }else {
            canvas.drawText(titleText, getWidth() / 2 - mTextRect.width() * 1.0f / 2, getHeight() - getPaddingBottom() / 2, paint);
        }

        //画图片
        rect.bottom -= mTextRect.height();

        if (mScaleType == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage,null,rect,paint);
        }else{
            rect.left = width / 2 - mImage.getWidth() / 2 ;
            rect.right = width / 2 + mImage.getWidth() / 2 ;

            rect.top = (height - mTextRect.height()) /2 - mImage.getHeight() / 2 ;
            rect.bottom = (height - mTextRect.height()) / 2 + mImage.getHeight() / 2 ;

            canvas.drawBitmap(mImage,null,rect,paint);
        }

    }


    private String RandomText(){
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4){
            set.add(random.nextInt(10));
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set){
            sb.append(i);
        }
        return sb.toString();
    }
}
