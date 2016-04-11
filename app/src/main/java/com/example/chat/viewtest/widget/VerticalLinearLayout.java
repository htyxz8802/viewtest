package com.example.chat.viewtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by htyxz on 2016/3/16.
 */
public class VerticalLinearLayout extends ViewGroup {

    //屏幕的高度
    private int mScreenHeght ;

    //手指按下是的getScrollY
    private int mScrollStart ;

    //手指抬起时getScrollY
    private int mScrollEnd ;

    //记录移动是的Y
    private int mLastY ;

    //滚动的辅组类
    private Scroller mScroller;

    //是否在滚动
    private boolean isScrolling = false ;

    //加速度检测
    private VelocityTracker mVelocityTraccker ;

    //记录当前页
    private int mCurrent = 0 ;


    public VerticalLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenHeght = metrics.heightPixels ;
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int cCount = getChildCount() ;

        for (int i = 0 ; i < cCount ; i++){
            View child = getChildAt(i) ;
            measureChild(child,widthMeasureSpec,mScreenHeght);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            int childCount = getChildCount() ;
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.height = mScreenHeght * childCount ;
            setLayoutParams(params);

            for (int i = 0 ; i < childCount ; i++){
                View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE){
                    childView.layout(l,i * mScreenHeght,r ,(i + 1)*mScreenHeght);
                }
            }

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction() ;
        int y = (int) event.getY();

        obtainVelocity(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                mLastY = y ;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = mLastY - y ;
                int scrollY = getScrollY() ;

                //已到达顶部，下拉多少，就上滚过少
                if (dy < 0 && scrollY + dy > 0){
                    dy = -scrollY ;
                }

                //已到达底部，上拉多少，就下滚多少
                if (dy > 0 && scrollY + dy > getHeight() - mScreenHeght){
                    dy = getHeight() - mScreenHeght - scrollY ;
                }
                scrollBy(0,dy);
                mLastY = y ;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY() ;
                int dScrollY = mScrollEnd - mScrollStart ;

                if (wantScrollToNext()){
                    if (shouldScrollToNext()){
                        mScroller.startScroll(0,getScrollY(),0,mScreenHeght-dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }
                }

                if (wantScrollToPre()){
                    if (shouldScrollToPre()){
                        mScroller.startScroll(0,getScrollY(),0,-mScreenHeght - dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0, -dScrollY);
                    }
                }
                isScrolling = true ;
                postInvalidate();
                recycleVelocity();
                break;
        }

        return true ;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }else{
            int position = getScrollY()/mScreenHeght ;
            if (mCurrent !=  position){
                if (null != listener){
                    mCurrent = position ;
                    listener.onPagerChanger(mCurrent);
                }
            }
        }
        isScrolling = false ;
    }

    /**
     * 初始化加速度检测器
     * @param event
     */
    void obtainVelocity(MotionEvent event){
        if (null == mVelocityTraccker){
            mVelocityTraccker = VelocityTracker.obtain() ;
        }
        mVelocityTraccker.addMovement(event);
    }

    void recycleVelocity(){
        if (null != mVelocityTraccker){
            mVelocityTraccker.recycle();
            mVelocityTraccker = null ;
        }
    }

    /**
     * 根据用户滚动，判断用户是否是滚动到下一页的意图
     * @return
     */
    private boolean wantScrollToNext(){
        return mScrollEnd > mScrollStart ;
    }
    /**
     * 根据滚动距离判断是否能够滚动到下一页
     *
     * @return
     */
    private boolean shouldScrollToNext()
    {
        return mScrollEnd - mScrollStart > mScreenHeght / 2 || Math.abs(getVelocityY()) > 600;
    }



    /**
     * 根据用户滚动，判断用户是否是滚动到下一页的意图
     * @return
     */
    private boolean wantScrollToPre(){
        return mScrollEnd < mScrollStart ;
    }
    /**
     * 根据用户滚动，判断用户是否有滚到到上一页的意图
     * @return
     */
   private boolean shouldScrollToPre(){
        return -mScrollEnd + mScrollStart > mScreenHeght /2 || Math.abs(getVelocityY()) > 600 ;
    }


    /**
     * 获取在Y方向的速度
     * @return
     */
    private int getVelocityY(){
        mVelocityTraccker.computeCurrentVelocity(1000);
        return (int) mVelocityTraccker.getYVelocity();
    }

    private OnPageChangeListener listener ;

    public void setOnPageChangeListener(OnPageChangeListener listener){
        this.listener = listener ;
    }

    public interface OnPageChangeListener{
        void onPagerChanger(int currentPage);
    }


}
