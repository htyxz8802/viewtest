package com.example.chat.viewtest.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by htyxz on 2016/3/22.
 */
public class ElasticScrollView extends ScrollView {

    private View inner ;
    private float y ;
    private Rect mRect = new Rect();


    public ElasticScrollView(Context context) {
        super(context);
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0){
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != inner) {
            onToucheEvents(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void onToucheEvents(MotionEvent ev){
        int action = ev.getAction() ;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float preY = y;
                float nowY = ev.getY();
                int deltay = (int) (preY - nowY);
                scrollBy(0,deltay);
                y = nowY;
                if (isNeedMove()){
                    if (mRect.isEmpty()){
                        mRect.set(inner.getLeft(),inner.getTop(),inner.getRight(),inner.getBottom());
                    }
                    inner.layout(inner.getLeft(),inner.getTop()- deltay,inner.getRight(),inner.getBottom() - deltay);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()){
                    animation() ;
                }
                break;
            default:
                break;
        }
    }



    private boolean isNeedAnimation(){
        return !mRect.isEmpty();
    }

    private void animation(){
        TranslateAnimation ta = new TranslateAnimation(0,0,inner.getTop(),mRect.top);
        ta.setDuration(500);
        inner.setAnimation(ta);
        inner.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        mRect.setEmpty();
    }

    /**
     * 判断当前是否是处于顶部或底部
     * @return
     */
    private boolean isNeedMove(){

        int offset = inner.getMeasuredHeight() - getHeight() ;
        int scrollY = getScrollY();
        Log.i("ElasticScrollViewjava","offset: " + offset+" ,scrollY: " + scrollY);
        if (scrollY == 0 || scrollY == offset){
            return true ;
        }
        return false ;
    }

}
