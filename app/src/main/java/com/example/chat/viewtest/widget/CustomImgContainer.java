package com.example.chat.viewtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by htyxz on 2016/3/16.
 */
public class CustomImgContainer extends ViewGroup{

    public CustomImgContainer(Context context) {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * 测量计算所有childeView 的宽度和高度，然后根据ChildView的计算结果，设置自己的宽度和高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**
         * 获取父容器，推荐给我们的宽和高，以及计算模式
         */
        int mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec) ;

        int mHeigthMode = MeasureSpec.getMode(heightMeasureSpec);
        int mHeigthSize = MeasureSpec.getSize(heightMeasureSpec) ;

        /**
         * 计算出所有的childView的宽和高
         */
        measureChildren(widthMeasureSpec,heightMeasureSpec);


        /**
         * 如果是wrap_content是设置的宽或高
         */
        int width = 0 ;
        int height = 0 ;

        int cCount = getChildCount() ;

        int cWidht = 0 ;
        int cHeight = 0 ;

        MarginLayoutParams cParams = null ;

        //用于计算左边两个childView的高度
        int lHeigth = 0 ;
        //用于计算右边两个childView的高度，最终高度去二者最大值
        int rHeight = 0 ;

        //用于计算上边两个childView的宽度
        int tWidth = 0 ;
        //用于计算下边两个childView的宽度，最终宽度去二者的最大值
        int bWidth = 0 ;

        for (int i = 0 ;i < cCount ; i++){
            View child = getChildAt(i) ;
            cWidht = child.getMeasuredWidth() ;
            cHeight = child.getMeasuredHeight() ;

            cParams = (MarginLayoutParams) child.getLayoutParams();

            if (i == 0 || i == 1){  //上面两个childView
                tWidth += cWidht + cParams.leftMargin + cParams.rightMargin ;
            }

            if (i == 2 || i ==3){   //下面的两个childview
                bWidth += cWidht + cParams.rightMargin + cParams.leftMargin ;
            }

            if(i == 0 || i == 2){
                lHeigth += cHeight + cParams.topMargin + cParams.bottomMargin ;
            }

            if (i == 1 || i == 3){
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin ;
            }
        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeigth , rHeight) ;

        /**
         * 如果是wrap_content，那么则就是我们计算后的值
         */
        setMeasuredDimension(mWidthMode == MeasureSpec.EXACTLY ? mWidthSize : width,
                mHeigthMode == MeasureSpec.EXACTLY ? mHeigthSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount() ;

        int cWidht = 0 ;
        int cHeight = 0 ;
        MarginLayoutParams cParams = null ;


        for (int i = 0 ; i < cCount ; i++){
            View child = getChildAt(i) ;
            cWidht = child.getMeasuredWidth() ;
            cHeight = child.getMeasuredHeight() ;
            cParams = (MarginLayoutParams) child.getLayoutParams();

            int ct = 0 ,cl = 0 ,cr = 0 ,cb = 0 ;

            switch (i){
                case 0 :
                    cl = cParams.leftMargin ;
                    ct = cParams.topMargin ;
                    break;
                case 1 :
                    cl = getWidth() - cWidht - cParams.leftMargin - cParams.rightMargin ;
                    ct = cParams.topMargin ;
                    break;
                case 2 :
                    cl = cParams.leftMargin ;
                    ct = getHeight() - cHeight - cParams.bottomMargin ;
                    break;
                case 3 :
                    cl = getWidth() - cWidht - cParams.leftMargin - cParams.rightMargin ;
                    ct = getHeight() -cHeight -cParams.bottomMargin ;
                    break;
            }

            cr = cl + cWidht ;
            cb = ct + cHeight ;
            child.layout(cl,ct,cr,cb);
        }


    }
}
