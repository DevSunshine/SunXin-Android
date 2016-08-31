package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class MaxGridView extends GridView{
    private int mNumColumns;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    public MaxGridView(Context context) {
        super(context);
    }

    public MaxGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        this.mNumColumns = numColumns;
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }

    @Override
    public void setVerticalSpacing(int verticalSpacing) {
        super.setVerticalSpacing(verticalSpacing);
        this.mVerticalSpacing = verticalSpacing;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void animateReorder(final int oldPosition, final int newPosition) {
        boolean isForward = newPosition > oldPosition;
        List<Animator> resultList = new LinkedList<Animator>();
        if (isForward) {
            for (int pos = oldPosition; pos < newPosition; pos++) {
                View view = getChildAt(pos);
                if ((pos + 1) % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,
                            -(view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1), 0,
                            view.getHeight() + mVerticalSpacing, 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            view.getWidth() + mHorizontalSpacing, 0, 0, 0));
                }
            }
        } else {
            for (int pos = oldPosition; pos > newPosition; pos--) {
                View view = getChildAt(pos);
                if ((pos) % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,
                            (view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1), 0,
                            -view.getHeight() - mVerticalSpacing, 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            -view.getWidth() - mHorizontalSpacing, 0, 0, 0));
                }
            }
        }

        AnimatorSet resultSet = new AnimatorSet();
        resultSet.playTogether(resultList);
        resultSet.setDuration(200);
        resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
        resultSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null){
                    listener.onAnimaterStart();
                }
//                mAnimationEnd = false;
                Log.v("zgy","==========onAnimationStart========") ;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                mAnimationEnd = true;
                Log.v("zgy","==========onAnimationEnd========") ;
                if (listener != null){
                    listener.onAnimaterend();
                }
            }
        });
        resultSet.start();
    }

    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    private OnAnimaterListener listener ;
    public void setOnAnimaterListener(OnAnimaterListener listener){
        this.listener = listener ;
    }

    public interface OnAnimaterListener{
        void onAnimaterStart() ;
        void onAnimaterend() ;
    }
}
