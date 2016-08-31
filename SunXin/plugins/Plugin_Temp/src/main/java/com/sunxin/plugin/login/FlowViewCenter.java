package com.sunxin.plugin.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon.zhong on 2015/1/23.
 */

public class FlowViewCenter extends ViewGroup {
    private List<List<View>> lines ;
    private List<View> lineViews ;

    public FlowViewCenter(Context context) {
        this(context, null);
    }

    public FlowViewCenter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowViewCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lineViews = new ArrayList<>() ;
        lines = new ArrayList<>() ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec) ;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec) ;
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) ;
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) ;

        int width = 0 ;
        int height = 0 ;

        int lineWidth = 0 ;
        int lineHeight = 0 ;
        int count = getChildCount() ;
        for(int i = 0 ; i < count ;i++){
            View child = getChildAt(i) ;
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams() ;
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin  ;
            int childHeight = child.getMeasuredHeight() + lp.topMargin  + lp.bottomMargin;


            if(lineWidth + childWidth> sizeWidth){
                height += lineHeight ;
                width = Math.max(width, lineWidth) ;
                lineWidth = childWidth ;
                lineHeight = childHeight ;
                List<View> lineView = new ArrayList<>() ;
                lineView.addAll(lineViews) ;
                lineViews.clear();
                lineViews.add(child) ;
                lines.add(lineView) ;
            }else {
                lineWidth += childWidth  ;
                lineHeight = Math.max(childHeight, lineHeight) ;
                lineViews.add(child) ;
            }
            width = Math.max(width, lineWidth) ;
            height = Math.max(height, lineHeight) ;

        }
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? widthMeasureSpec:MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                modeHeight == MeasureSpec.EXACTLY? heightMeasureSpec:MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth() ;
        int childLeft = 0 ;
        int childTop = 0 ;
        int layoutWidth = 0;
        int layoutHeight = 0;
        int lineHeight = 0 ;

        int cCount = getChildCount() ;
        for(int i = 0; i < cCount ;i ++){
            View child = getChildAt(i) ;
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams() ;
            layoutWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin ;
            layoutHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin  ;

            if(childLeft + layoutWidth > width){
                childLeft = 0 ;
                childTop +=  lineHeight  ;
                lineHeight = layoutHeight ;
            }

            setChildFrame(child,childLeft+lp.leftMargin,childTop+lp.topMargin,child.getMeasuredWidth(),child.getMeasuredHeight()) ;
            childLeft += layoutWidth ;
            lineHeight = Math.max(lineHeight, layoutHeight) ;
        }
    }
    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
