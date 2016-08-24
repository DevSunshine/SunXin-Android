package com.sunshine.sunxin.widget.checkbox;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.sunshine.sunxin.util.ColorUtil;

public abstract class CheckStateDrawable extends Drawable {
    private ColorStateList mColorStateList;
    private int mColor;
    private int mAlpha = 255;

    protected boolean mChecked = false;
    protected boolean mEnabled = true;

    protected boolean mInEditMode = false;
    protected boolean mAnimator ;

    public CheckStateDrawable(ColorStateList tintStateList) {
        super();
        setColorStateList(tintStateList);
    }

    public void inEditMode(boolean inEditMode) {
        mInEditMode = inEditMode;
    }

    @Override
    public boolean isStateful() {
        return (mColorStateList.isStateful()) || super.isStateful();
    }


    @Override
    public boolean setState(int[] stateSet) {
        if (stateSet == null)
            return false;

        // Call super
        boolean status = super.setState(stateSet);

        boolean oldChecked = mChecked;
        boolean oldEnabled = mEnabled;
        mChecked = false;
        mEnabled = true;
        for (int i : stateSet) {
            if (i == android.R.attr.state_checked) {
                mChecked = true;
            } else if (i == -android.R.attr.state_enabled) {
                mEnabled = false;
            }
        }

        if (status || oldChecked != mChecked || oldEnabled != mEnabled) {
            //We've changed states
            onStateChange(getColor(), oldChecked, mChecked);
            invalidateSelf();
        }

        return status;
    }

    public void setAnimator(boolean animator){
        this.mAnimator = animator ;
    }
    /**
     * Update tint color
     *
     * @param state tint stats
     * @return is changed
     */
    @Override
    protected boolean onStateChange(int[] state) {
        final int color = state == null ? mColorStateList.getDefaultColor() : mColorStateList.getColorForState(state, mColor);
        boolean bFlag = mColor != color;
        if (bFlag) {
            mColor = color;
        }
        return bFlag;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    /**
     * Set the alpha level for this drawable [0..255]. Note that this drawable
     * also has a color in its paint, which has an alpha as well. These two
     * values are automatically combined during drawing. Thus if the color's
     * alpha is 75% (i.e. 192) and the drawable's alpha is 50% (i.e. 128), then
     * the combined alpha that will be used during drawing will be 37.5% (i.e.
     * 96).
     */
    @Override
    public void setAlpha(int alpha) {
        if (alpha != mAlpha) {
            mAlpha = alpha;
            onStateChange(getState());
        }
    }

    /**
     * Set color trans to ColorStateList
     *
     * @param color Color
     */
    public void setColor(int color) {
        setColorStateList(ColorStateList.valueOf(color));
    }

    /**
     * Set the Tint ColorStateList
     *
     * @param tintStateList ColorStateList
     */
    public void setColorStateList(ColorStateList tintStateList) {
        if (tintStateList == null)
            tintStateList = ColorStateList.valueOf(Color.BLACK);
        mColorStateList = tintStateList;
        onStateChange(getState());
    }

    /**
     * Get the Tint ColorStateList
     *
     * @return mTintStateList
     */
    public ColorStateList getColorStateList() {
        return mColorStateList;
    }

    public int getColor() {
        return modulateColorAlpha(mColor);
    }

    public int getCheckedColor() {
        int[] status = new int[]{
                mEnabled ? android.R.attr.state_enabled : -android.R.attr.state_enabled,
                android.R.attr.state_checked};
        int color = mColorStateList.getColorForState(status, mColor);
        color = modulateColorAlpha(color);
        return color;
    }

    public int getUnCheckedColor() {
        int[] status = new int[]{
                mEnabled ? android.R.attr.state_enabled : -android.R.attr.state_enabled,
                -android.R.attr.state_checked};
        int color = mColorStateList.getColorForState(status, mColor);
        color = modulateColorAlpha(color);
        return color;
    }

    protected int modulateColorAlpha(int color) {
        if (mAlpha < 255) {
            return ColorUtil.modulateColorAlpha(color, mAlpha);
        } else {
            return color;
        }
    }

    public boolean isChecked() {
        return mChecked;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    protected abstract void onStateChange(int color, boolean oldChecked, boolean newChecked);
}