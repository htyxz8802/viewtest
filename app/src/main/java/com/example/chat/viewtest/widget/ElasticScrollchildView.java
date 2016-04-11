package com.example.chat.viewtest.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by htyxz on 2016/3/22.
 */
public class ElasticScrollchildView extends ScrollView {

    private View inner ;
    private float y ;
    private Rect mRect = new Rect();

    private static final int MAX_Y_OVERSCROLL_DISPLAY = 2000;

    private int mMaxOverScrollDistance = 0;

    public ElasticScrollchildView(Context context) {
        super(context);
        initView(context);
    }

    public ElasticScrollchildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ElasticScrollchildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ElasticScrollchildView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    void initView(Context context){
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density ;
        mMaxOverScrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISPLAY);
        Log.i("ElasticScrollchildviewjava", "mMaxOverScrollDistance: " + mMaxOverScrollDistance);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverScrollDistance, isTouchEvent);
    }
}
