package com.sunshine.sunxin.view;// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunshine.sunxin.R;


/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */


/**
 * 左边返回按钮的格式 <返回,最多可现实两个按钮，<返回 关闭
 * <p/>
 * 中间显示为标题，标题显示不全用...代替，也可以添加自定义布局
 * <p/>
 * 右边留出两个按钮的位置，可以是文字或者图标；最多添加两个，多了不生效
 *
 * 所有的 btn 都是TextView，强转的话可以不需要判断，但是不能转其他类型
 */
public class TitleView extends LinearLayout {
    private static final int BTN_TEXT_SIZE = 16 ;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RelativeLayout mContent;
    private int mTitleViewHeight;
    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;
    private LinearLayout mMiddleLayout;
    private TextView mTitle ;
    private TextView mSubTitle ;
    private int mLeftWidth;
    private int mRightWidth;
    private int mMiddleWidth ;
    private View mTitleLine ;

    private int mBtnHeight;
    private float mDensity;


    public TitleView(Context context) {
        super(context);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mContent = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.title_view, this, false);
        addView(mContent);
        mLeftLayout = (LinearLayout) findViewById(R.id.id_title_left_layout);
        mRightLayout = (LinearLayout) findViewById(R.id.id_title_right_layout);
        mMiddleLayout = (LinearLayout) findViewById(R.id.id_title_middle_layout);
        mTitle = (TextView) findViewById(R.id.id_title);
        mSubTitle = (TextView) findViewById(R.id.id_sub_title);
        mTitleLine =  findViewById(R.id.id_title_line);
        mDensity = getResources().getDisplayMetrics().density;
        mBtnHeight = (int) (getResources().getDisplayMetrics().density * 48 + 0.5);
        mPaint.setTextSize(BTN_TEXT_SIZE * mDensity);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTitleViewHeight = h;
        Log.v("zgy", "=============mTitleViewHeight=" + mTitleViewHeight);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public TextView addLeftBtn(String backText, OnClickListener listener) {
        return innerAddLeftBtn(0, backText, listener);
    }
    public TextView addRightBtn(String backText, OnClickListener listener) {
        return innerAddRightBtn(0, backText, listener);
    }
    public TextView addRightBtn(int iconId,String backText, OnClickListener listener) {
        return innerAddRightBtn(iconId, backText, listener);
    }
    public TextView addRightBtn(int iconId, OnClickListener listener) {
        return innerAddRightBtn(iconId, "", listener);
    }
    public TextView addLeftBtn(int iconId,String backText,OnClickListener listener){
        return innerAddLeftBtn(iconId, backText, listener);
    }

    public TextView addLeftBtn(int iconId,OnClickListener listener){
        return innerAddLeftBtn(iconId, "", listener);
    }

    public TextView getBackBtn(){
        if (mLeftLayout.getChildCount() == 0){
            throw new RuntimeException("please add back btn first!") ;
        }
        return (TextView) mLeftLayout.getChildAt(0);
    }

    public void hideTitleLine(){
        mTitleLine.setVisibility(GONE);
    }

    public void showTitleLine(){
        mTitleLine.setVisibility(VISIBLE);
    }

    public boolean removeBtn(TextView btn){
        for (int i = 0 ; i < mLeftLayout.getChildCount(); i++){
            TextView item = (TextView) mLeftLayout.getChildAt(i);
            if (item == btn){
                mLeftLayout.removeView(item);
                measureLeftView();
                return true ;
            }
        }
        for (int i = 0 ; i < mRightLayout.getChildCount(); i++){
            TextView item = (TextView) mRightLayout.getChildAt(i);
            if (item == btn){
                mRightLayout.removeView(item);
                measureRightView();
                return true ;
            }
        }
        return false ;
    }

    public void removeLeftBtns(){
        mLeftLayout.removeAllViews();
    }

    public void removeRightBtns(){
        mRightLayout.removeAllViews();
    }

    public TextView setTitle(String title){
        mTitle.setText(title);
        return mTitle ;
    }
    public TextView setSubTitle(String title){
        if (mSubTitle.getVisibility() !=View.VISIBLE){
            mSubTitle.setVisibility(VISIBLE);
        }
        mSubTitle.setText(title);
        return mSubTitle ;
    }
    public void setTitlePosition(float dy){
        float current = ViewCompat.getTranslationY(mTitle) ;
        if (current == 0 && dy<0){
            return;
        }
        float trans = (current + dy < 0) ? 0 : current + dy ;
        ViewCompat.setTranslationY(mTitle, trans);
    }

    public void setTitleBottomHide(){
        float textHeight = mTitle.getMeasuredHeight() ;
        float trans = (getHeight()+textHeight)/2.0f ;
        ViewCompat.setTranslationY(mTitle,trans);
    }
    public TextView setTitle(int resId){
        mTitle.setText(resId);
        return mTitle ;
    }
    public TextView setSubTitle(int resId){
        if (mSubTitle.getVisibility() !=View.VISIBLE){
            mSubTitle.setVisibility(VISIBLE);
        }
        mSubTitle.setText(resId);
        return mSubTitle ;
    }

    public void addCustomerView(View view){
        if (mTitle.getVisibility() == VISIBLE){
            mTitle.setVisibility(GONE);
        }
        if (mSubTitle.getVisibility() ==View.VISIBLE){
            mSubTitle.setVisibility(GONE);
        }
        mMiddleLayout.addView(view);
    }

    private TextView innerAddLeftBtn(int iconId ,String backText, OnClickListener listener) {
        TextView leftBtn;
        if (mLeftLayout.getChildCount() > 0) {
            TextView first = (TextView) mLeftLayout.getChildAt(0);
            CharSequence txt =  first.getText();
            int width = (int) mPaint.measureText(txt,0,txt.length());
            if (width > mBtnHeight*1.5f){
                return first ;
            }

        }
        if (mLeftLayout.getChildCount() < 2){
            int textWidth = (int) mPaint.measureText(backText,0,backText.length());
            int viewWidth = 0;
            if (textWidth > mBtnHeight*2){
                viewWidth = mBtnHeight*2 ;
            }
            leftBtn = new TextView(getContext());
            LayoutParams params =
                    new LayoutParams(viewWidth == 0 ?
                            LayoutParams.WRAP_CONTENT:viewWidth, mBtnHeight);
            params.gravity = Gravity.CENTER;
            leftBtn.setGravity(Gravity.CENTER_VERTICAL);
            int leftPadding = (int) (8 * mDensity + 0.5f);
            leftBtn.setPadding(leftPadding, 0, 0, 0);
            leftBtn.setTextColor(ContextCompat.getColorStateList(getContext().getApplicationContext(), R.color.white_color_selector));
            leftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, BTN_TEXT_SIZE);
            leftBtn.setClickable(true);
            leftBtn.setMaxLines(1);
            leftBtn.setEllipsize(TextUtils.TruncateAt.END);
            leftBtn.setMaxWidth(2 * mBtnHeight);
            mLeftLayout.addView(leftBtn, params);
        }else {
            leftBtn = (TextView) mLeftLayout.getChildAt(1);
        }
        if (iconId != 0){
            int padding = (int) (2 * mDensity + 0.5f);
//            leftBtn.setCompoundDrawablePadding(padding);
            leftBtn.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
        }
        leftBtn.setText(backText);
        leftBtn.setOnClickListener(listener);
        measureLeftView();
        return leftBtn;
    }

    private TextView innerAddRightBtn(int iconId,String backText, OnClickListener listener) {
        TextView rightBtn;
        if (mRightLayout.getChildCount() < 2) {
            int textWidth = (int) mPaint.measureText(backText,0,backText.length());
            int viewWidth = 0;
            if (textWidth > mBtnHeight){
                viewWidth = mBtnHeight ;
            }
            rightBtn = new TextView(getContext());
            LayoutParams params =
                    new LayoutParams(viewWidth == 0 ?
                            LayoutParams.WRAP_CONTENT:viewWidth, mBtnHeight);
            params.gravity = Gravity.CENTER;
            rightBtn.setGravity(Gravity.CENTER_VERTICAL);
            int rightPadding = (int) (8 * mDensity + 0.5f);
            rightBtn.setPadding(0, 0, rightPadding, 0);
            rightBtn.setMaxLines(1);
            rightBtn.setEllipsize(TextUtils.TruncateAt.END);
            rightBtn.setMaxWidth(2 * mBtnHeight);
            rightBtn.setTextColor(ContextCompat.getColorStateList(getContext().getApplicationContext(), R.color.white_color_selector));
            rightBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, BTN_TEXT_SIZE);

            rightBtn.setClickable(true);
            mRightLayout.addView(rightBtn, 0, params);
            Log.v("zgy", "============mRightLayout.getMeasuredWidth()" + mRightLayout.getMeasuredWidth());
        } else {
            rightBtn = (TextView) mRightLayout.getChildAt(1);
        }
        if (iconId != 0){
            int padding = (int) (2 * mDensity + 0.5f);
            rightBtn.setCompoundDrawablePadding(padding);
            rightBtn.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
        }
        rightBtn.setText(backText);
        rightBtn.setOnClickListener(listener);
        measureRightView();
        return rightBtn;
    }

    public void hideBackBtn() {
        mLeftLayout.removeAllViews();
    }


    private void measureLeftView() {
        mLeftLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLeftWidth = mLeftLayout.getMeasuredWidth();
                mLeftLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                resetMiddleLayout();
                return true;
            }
        });
    }

    private void measureRightView() {
        mRightLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRightWidth = mRightLayout.getMeasuredWidth();
                mRightLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                resetMiddleLayout();
                return true;
            }
        });
    }

    private void resetMiddleLayout() {
        mMiddleWidth = getMeasuredWidth() - 2*Math.max(mRightWidth,mLeftWidth) ;
//        mMiddleParent.setGravity(mRightWidth > mLeftWidth ? Gravity.RIGHT : Gravity.LEFT);
        ViewGroup.LayoutParams middleParam = mMiddleLayout.getLayoutParams() ;
        middleParam.width = mMiddleWidth ;
        mMiddleLayout.setLayoutParams(middleParam);
        //如果是自定义View，不显示标题
//        if (mMiddleLayout.getChildCount() < 2){
//            mTitle.setVisibility(VISIBLE);
//        }
    }



}
