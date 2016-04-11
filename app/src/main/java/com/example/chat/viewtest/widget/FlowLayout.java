package com.example.chat.viewtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htyxz on 2016/3/16.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //参数请求
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidthMode = MeasureSpec.getMode(widthMeasureSpec) ;
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        //记录最终的所有控件所占的宽高
        int width = 0 ;
        int height = 0 ;

        //记录每行的宽度，取最大值，累积到width
        int lineWidth = 0 ;

        //记录每行的行高，累积到height
        int lineHeight = 0 ;

        int childCount = getChildCount() ;

        for (int i=0 ;i < childCount ; i++){
            View child = getChildAt(i) ;
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getWidth() + lp.leftMargin + lp.rightMargin; //获取childview实际所占的宽度
            int childHeight = child.getHeight() + lp.topMargin + lp.bottomMargin; //获取childview实际所占的高度

            //如果加入当前的childview，就超过最大的宽度,需要换行
            if (lineWidth + childWidth > mWidthSize){
                width = Math.max(lineWidth,childWidth) ;
                lineWidth = childWidth ;
                lineHeight += childHeight ;
            }else{
                lineWidth += childWidth ;
                lineHeight = Math.max(childHeight,lineHeight) ;
            }

            if (i == childCount -1){
                width = Math.max(lineWidth,width);
                height +=lineHeight;
            }
        }

        setMeasuredDimension(mWidthMode == MeasureSpec.EXACTLY ? mWidthSize : width,
                mHeightMode == MeasureSpec.EXACTLY ? mHeightSize : height);
    }

    //存储每行的每个view
    private List<List<View>> mAllViews = new ArrayList<>();

    //存储每行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth() ;

        int lineWidth = 0 ;
        int lineHeight = 0 ;

        List<View> lineViews = new ArrayList<>();

        int childCount = getChildCount() ;
        for (int i =0 ;i < childCount ; i++){
            View childview = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) childview.getLayoutParams();

            int childWidth = childview.getMeasuredWidth() ;
            int childHeight = childview.getMeasuredHeight() ;

            //需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width ){
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineWidth = 0 ;
                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth + lp.leftMargin + lp.rightMargin ;
            lineHeight = Math.max(lineHeight,childHeight + lp.topMargin + lp.bottomMargin)  ;
            lineViews.add(childview) ;
        }
        //记录最后一行的view
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);


        int left = 0 ;
        int top = 0 ;
        int numSize = mAllViews.size() ;
        for (int j=0 ; j < numSize ; j++){
            lineViews = mAllViews.get(j) ;
            lineHeight = mLineHeight.get(j);

            Log.e("FlowLayout", "第" + j + "行 ：" + lineViews.size() + " , " + lineViews);
            Log.e("FlowLayout", "第" + j + "行， ：" + lineHeight);

            int lineSize = lineViews.size() ;
            for (int k =0 ; k < lineSize ; k++){
                View child = lineViews.get(k);
                if (child.getVisibility() == View.GONE){
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin ;
                int tc = top + lp.topMargin ;
                int rc = lc + child.getMeasuredWidth() ;
                int bc = tc + child.getMeasuredHeight() ;

                child.layout(lc,tc,rc,bc);

                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin ;
            }
            left = 0 ;
            top += lineHeight ;
        }
    }
}
