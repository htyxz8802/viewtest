package com.example.chat.viewtest.UI;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.chat.viewtest.R;

public class AnimatorActivity extends AppCompatActivity {

    private ImageView iv_ball ;

    int mScreenHeight ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        init();


    }

    void init(){
        iv_ball = (ImageView) findViewById(R.id.iv_ball);

         WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
         mScreenHeight = wm.getDefaultDisplay().getWidth();

    }


    public void rotateAnimRun(final View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "zhy", 1.0f, 0.0f).setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                view.setAlpha(val);
                view.setScaleX(val);
                view.setScaleY(val);
            }
        });
//        propertyValuesHolder(view);
    }

    public void propertyValuesHolder(View view){
        PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("alpha",1f,0f,1f);
        PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat("scaleX",1f,0f,1f);
        PropertyValuesHolder pvhz = PropertyValuesHolder.ofFloat("scaleY",1f,0f,1f);


        ObjectAnimator.ofPropertyValuesHolder(view,pvhx,pvhy,pvhz).setDuration(500).start();
    }


    public void verticalRun(View view){
        ValueAnimator animator = ValueAnimator.ofFloat(0, mScreenHeight - iv_ball.getHeight());
        animator.setTarget(iv_ball);
        animator.setDuration(1000);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv_ball.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

    }
    public void paowuxian(View view){

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
//        valueAnimator.setInterpolator(new LinearInterpolator());//动画从开始到结束，变化率是线性变化
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//动画从开始到结束，变化率是先加速后减速的过程
//        valueAnimator.setInterpolator(new AccelerateInterpolator());//动画从开始到结束，变化率是一个加速的过程
//        valueAnimator.setInterpolator(new DecelerateInterpolator());//动画从开始到结束，变化率是一个减速的过程。
        valueAnimator.setInterpolator(new CycleInterpolator(5));//动画从开始到结束，变化率是循环给定次数的正弦曲线。
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                Log.e("AnimatorActivty", "fraction: " + fraction);
                PointF mPoint = new PointF();
                mPoint.x = 200 * fraction * 3;
//                mPoint.y = 0.5f * 200 * fraction * 3;
                mPoint.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return mPoint;
            }
        });

        valueAnimator.start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF mPointf = (PointF) animation.getAnimatedValue();
                iv_ball.setX(mPointf.x);
                iv_ball.setY(mPointf.y);
            }
        });

    }

    public void alpha(View view){

    }


}
