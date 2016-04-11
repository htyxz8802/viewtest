package com.example.chat.viewtest.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.chat.viewtest.ZoomActivity;

/**
 * Created by htyxz on 2016/3/18.
 */
public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener , ViewTreeObserver.OnGlobalLayoutListener{

    private static final float SCALE_MAX = 4.0f;
    private static final float SCALE_MIN = 1.0f;

    //初始化缩放比例，图片宽高大于屏幕的宽高，则小于1
    private float initScale = 1.0f ;

    //存放矩阵的9个值
    private float[] matrixValues = new float[9];

    private boolean once = true ;

    private boolean isAutoScale = false ;

    //缩放的手势检测
    private ScaleGestureDetector mScaleGestureDetector = null ;

    private GestureDetector mGestureDetector = null ;

    private final Matrix mScaleMatrix = new Matrix();

    public ZoomImageView(Context context) {
        super(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context,this);
        this.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if (isAutoScale){
                    return true ;
                }
                float x = e.getX() ;
                float y = e.getY() ;
                if (getScale() < SCALE_MIN){
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MIN,x,y) ,16);
                }if (getScale() >= SCALE_MIN && getScale() < SCALE_MAX){
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MAX,x,y) ,16);
                }else{
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                }
                isAutoScale = true ;
                return super.onDoubleTap(e);
            }
        });

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() ;
    }




    //OnScaleGestureListener
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale() ;
        float scaleFactor = detector.getScaleFactor() ;

        if (null == getDrawable()){
            return true ;
        }
        /**
         * 缩放的范围控制
         */
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)){

            /**
             * 最大值最小值判断
             */
            if (scaleFactor * scale < initScale){
                scaleFactor = initScale / scale ;
            }

            if (scaleFactor * scale > SCALE_MAX){
                scaleFactor = SCALE_MAX / scale ;
            }

            mScaleMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());
            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);
        }


        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }



//OnTouchListener

    private int lastPointCount = 0 ;
    private float mLastX = 0 , mLastY = 0 ;

    private boolean isCanDrag = true ;

    private int mTouchSlop = 0 ;

    private boolean  isCheckLeftAndRight = false , isCheckTopAndBottom = false;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mScaleGestureDetector.onTouchEvent(event)){
            return true ;
        }

        float x = 0 , y = 0 ;

        final int pointCount = event.getPointerCount() ;

        //求 x、y的平均值
        for (int i = 0 ; i < pointCount ; i++){
            x += event.getX(i) ;
            y += event.getY(i) ;
        }
        x = x / pointCount ;
        y = y / pointCount ;

        if (pointCount != lastPointCount){
            isCanDrag = false ;
            mLastX = x ;
            mLastY = y ;
        }

        lastPointCount = pointCount ;


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                float dx = x - mLastX;
                float dy = y - mLastY ;

                if (!isCanDrag){
                    isCanDrag = isCanDrag(dx,dy);
                }

                if (isCanDrag){
                    RectF rectF = getMatrixRectF() ;
                    if (null != getDrawable()){
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        if (rectF.width() < getWidth()){
                            dx = 0 ;
                            isCheckLeftAndRight = false ;
                        }
                        if (rectF.height() < getHeight()){
                            dy = 0 ;
                            isCheckTopAndBottom = false ;
                        }
                        mScaleMatrix.postTranslate(dx,dy);
                        checkMatrixBounds();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x ;
                mLastY = y ;
                break ;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointCount = 0 ;
                break;


        }


        return true ;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    //OnGlobalLayoutListener
    @Override
    public void onGlobalLayout() {
        if (once){
            Drawable drawable = getDrawable() ;
            if (null == drawable){
                return ;
            }

            int width = getWidth() ;
            int height = getHeight() ;

            int dw = drawable.getIntrinsicWidth() ;
            int dh = drawable.getIntrinsicHeight() ;

            float scale = 1.0f ;
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height){
                scale = width * 1.0f / dw ;
            }
            if (dh > height && dw <= width){
                scale = height * 1.0f / dh ;;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height){
                scale = Math.min(width * 1.0f /dw,height * 1.0f / dh) ;
            }
            initScale = scale;

            mScaleMatrix.postTranslate((width - dw)/2,(height- dh) / 2);

            mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);

            setImageMatrix(mScaleMatrix);
            once = false ;


        }
    }

    /**
     * 获取当前的缩放比
     * @return
     */
    private float getScale(){
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X] ;
    }

    /**
     * 在缩放时，进行图片显示范围的控制
     */
    private void checkBorderAndCenterWhenScale()
    {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width)
        {
            if (rect.left > 0)
            {
                deltaX = -rect.left;
            }
            if (rect.right < width)
            {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height)
        {
            if (rect.top > 0)
            {
                deltaY = -rect.top;
            }
            if (rect.bottom < height)
            {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width)
        {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height)
        {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF()
    {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d)
        {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }



    /**
     * 移动时，进行边界判断，主要判断宽或高大于屏幕的
     */
    private void checkMatrixBounds(){
        RectF rectF = getMatrixRectF() ;
        float deltaX = 0 , deltaY = 0  ;

        final float viewWidth = getWidth() ;
        final  float viewHeight = getHeight() ;
        // 判断移动或缩放后，图片显示是否超出屏幕边界
        if (rectF.top > 0 && isCheckTopAndBottom){
            deltaY = - rectF.top ;
        }

        if (rectF.bottom < viewHeight && isCheckTopAndBottom){
            deltaY = viewHeight - rectF.bottom ;
        }

        if (rectF.left > 0 && isCheckLeftAndRight){
            deltaX = - rectF.left ;
        }
        if (rectF.right < viewWidth && isCheckLeftAndRight){
            deltaX = viewWidth - rectF.right ;
        }

        mScaleMatrix.setTranslate(deltaX,deltaY);
    }

    /**
     * 是否有推动行为
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx , float dy){
        return Math.sqrt((dx * dx) + dy * dy ) >= mTouchSlop ;
    }

    /**
     * 自动缩放的任务
     *
     * @author zhy
     *
     */
    private class AutoScaleRunnable implements Runnable
    {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y)
        {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale)
            {
                tmpScale = BIGGER;
            } else
            {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run()
        {
            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            final float currentScale = getScale();
            //如果值在合法范围内，继续缩放
            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale)))
            {
                ZoomImageView.this.postDelayed(this, 16);
            } else//设置为目标的缩放比例
            {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }

        }
    }

}
