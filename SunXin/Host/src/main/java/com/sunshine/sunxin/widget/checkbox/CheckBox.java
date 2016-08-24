package com.sunshine.sunxin.widget.checkbox;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.sunshine.sunxin.R;

/**
 * sssssssssssssssssssssssssss 钟光燕
 */
public class CheckBox extends android.widget.CheckBox {
    private CircleCheckDrawable mMarkDrawable;

    public CheckBox(Context context) {
        super(context);
        init();
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CheckBox.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CheckBox.class.getName());
    }

    private void init() {
        mMarkDrawable = new CircleCheckDrawable(getResources().getDisplayMetrics().density ,
                ContextCompat.getColorStateList(getContext().getApplicationContext(),R.color.check_color_selector));
        setButtonDrawable(mMarkDrawable);
    }

    public void setBorderSize(int size) {
        mMarkDrawable.setBorderSize(size);
    }

    public void setIntervalSize(int size) {
        mMarkDrawable.setIntervalSize(size);
    }

    public void setMarkSize(int size) {
        mMarkDrawable.setMarkSize(size, true);
    }

    public int getBorderSize() {
        return mMarkDrawable.getBorderSize();
    }

    public int getIntervalSize() {
        return mMarkDrawable.getIntervalSize();
    }

    public int getMarkSize() {
        return mMarkDrawable.getMarkSize();
    }

    public void setMarkColor(int color) {
        mMarkDrawable.setColor(color);
    }

    public void setMarkColor(ColorStateList colorList) {
        mMarkDrawable.setColorStateList(colorList);
    }


    public void setChecked(boolean checked,boolean anim){
        mMarkDrawable.setAnimator(anim);
        setChecked(checked);
    }

    public ColorStateList getMarkColor() {
        return mMarkDrawable.getColorStateList();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Refresh display with current params
        refreshDrawableState();
    }
}