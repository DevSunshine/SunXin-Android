/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.sunxin.plugin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/3/23.
 * ===================================================
 * <p>
 * 拖动交换item位置
 * <p>
 * ===================================================
 */
public class SwapGridView extends GridView {

    private Context mContext;
    private IDragAdapter mDragAdapter;
    private boolean isDrag = false;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private int mDragPosition;
    private int mTempDragPosition ;
    private int mCurrentPosition ;
    private View mDragView ;
    private List<View> mTempView = new LinkedList<>();
    private View mStartDragItemView = null;
    private ImageView mDragImageView;
    private Vibrator mVibrator;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private Bitmap mDragBitmap;
    private int mPoint2ItemLeft;
    private int mPoint2ItemTop;
    private int mOffset2Top;
    private int mOffset2Left;
    private int mTopBound;
    private boolean mAnimationEnd = true;
    private int mNumColumns;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private boolean mDragMode;
    private OnDragListener mDragListener;
    private int mShadowWidth;
    private boolean mUnableMove ;
    private float mDensity ;

    public SwapGridView(Context context) {
        super(context);
        initView(context);
    }

    public SwapGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwapGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwapGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);

    }

    private void initView(Context context) {
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(new ItemLongClick());
        mDensity = getResources().getDisplayMetrics().density ;
        mShadowWidth = (int) (mDensity*4.0f);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof IDragAdapter) {
            mDragAdapter = (IDragAdapter) adapter;
        }else {
//            throw new RuntimeException("擦擦，你会不会啊！");
        }

    }

    public void setDragMode(boolean dragMode) {
        this.mDragMode = dragMode;
    }

    public void setTopBound(int topBound) {
        mTopBound = topBound;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    private class ItemLongClick implements OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (!mDragMode||position >= mDragAdapter.unableMove()){
                return false ;
            }
            /*获取Item的View*/
            mStartDragItemView = getViewForPosition(position);
            mDragPosition = (int) mStartDragItemView.getTag();
            /*通过View创建一个Drawable*/
            mVibrator.vibrate(50);
            mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
            mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

            mStartDragItemView.setDrawingCacheEnabled(true);
            mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
            mStartDragItemView.destroyDrawingCache();
            mDragBitmap =  createBitmap(mStartDragItemView) ;
            createDragImage(mDragBitmap, mDownX, mDownY);
            mStartDragItemView.setVisibility(View.INVISIBLE);
            ViewCompat.setTranslationX(mStartDragItemView,getMeasuredWidth());
            mStartDragItemView.setTag(Integer.MAX_VALUE >> 2);
            /*把拖动标志设为true*/
            isDrag = true;
            if (mDragListener != null)
                mDragListener.startDrag();
            return true;
        }
    }

    private Bitmap createBitmap(View view){
        /*首先创建一个Bitmap，这个位图为空位图，里面啥也没有*/
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth() + mShadowWidth - (int)(mDensity*1.0f), view.getHeight()+ mShadowWidth -
                (int)(mDensity*1.0f), Bitmap.Config.ARGB_8888);
        /*创建一个画布，把空位图放入画布中*/
        Canvas canvas = new Canvas(bitmap);
        /*把所选的item的View 绘制到画布上，其实也就是会知道了Bitmap上*/
        canvas.translate( (mShadowWidth - mDensity*1.0f)/2.0f, (mShadowWidth - mDensity*1.0f)/2.0f);
        view.draw(canvas);
        /*绘制边矩形*/
        return bitmap ;
    }

    /**
     * 通过位置获取View
     *
     * @param position
     * @return
     */
    public View getViewForPosition(int position) {
        int itemPosition = position - getFirstVisiblePosition();
        View view = getChildAt(itemPosition);
        return view;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = (int) ev.getX();
            mDownY = (int) ev.getY();
            mOffset2Top = (int) (ev.getRawY() - mDownY);
            mOffset2Left = (int) (ev.getRawX() - mDownX);
        }

        if (!mDragMode) {
            return super.onTouchEvent(ev);
        }
        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    onStopDrag();
                    isDrag = false;
                    if (mDragListener != null)
                        mDragListener.stopDrag();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void onStopDrag() {
        mDragAdapter.hideItem(-1);
        removeDragImage();
    }

    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    private void onSwapItem(int moveX, int moveY) {
        if (!mAnimationEnd) return;
        int tempPosition = pointToPosition(moveX, moveY);
        View view = getChildAt(tempPosition);
        if (view == null){
            return;
        }
        mCurrentPosition = (int) view.getTag();
        if (mCurrentPosition == Integer.MAX_VALUE >> 2){
            return;
        }
        mUnableMove = false ;
        if (tempPosition >= mDragAdapter.unableMove()){
            mUnableMove = true ;
        }
        if (mCurrentPosition != mDragPosition && mAnimationEnd&&!mUnableMove) {
            mTempDragPosition = mDragPosition ;
            mDragAdapter.swapItem(mDragPosition, mCurrentPosition);
            animateReorder(mDragPosition, mCurrentPosition);
            mDragPosition = mCurrentPosition;
        }
    }

    public View getChildForTag(int tag) {
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            int childTag = (int) child.getTag();
            if (childTag == tag) {
              return child ;
            }
        }
        return null;
    }
    private void animateReorder(final int oldPosition, final int newPosition) {
        boolean isForward = newPosition > oldPosition;
        List<Animator> resultList = new LinkedList<Animator>();
        if (isForward) {
            for (int pos = oldPosition; pos < newPosition; pos++) {
                int nextPosition = pos + 1;
                View view = getChildForTag(nextPosition);
                if (view == null){
                    continue;
                }
                float startX = ViewCompat.getTranslationX(view) ;
                float startY = ViewCompat.getTranslationY(view) ;
                if (nextPosition % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,startX,
                            (view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1),startY,
                            -(view.getHeight() + mVerticalSpacing)));

                } else {
                    resultList.add(createTranslationAnimations(view,startX,
                            -view.getWidth() - mHorizontalSpacing , 0, 0));

                }
            }
        } else {
            for (int pos = oldPosition; pos > newPosition; pos--) {
                int nextPosition = pos - 1;
                View view = getChildForTag(nextPosition);
                if (view == null){
                    continue;
                }
                float startX = ViewCompat.getTranslationX(view) ;
                float startY = ViewCompat.getTranslationY(view) ;
                if ((nextPosition + 1) % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,startX,
                            -(view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1), startY,
                            view.getHeight() + mVerticalSpacing));

                } else {
                    resultList.add(createTranslationAnimations(view, startX,
                            view.getWidth() + mHorizontalSpacing, 0, 0));
                }
            }
        }

        AnimatorSet resultSet = new AnimatorSet();
        resultSet.playTogether(resultList);
        resultSet.setDuration(250);
        resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
        resultSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                refreshTagPosition() ;
                mAnimationEnd = true;
            }
        });
        resultSet.start();
    }

    private void refreshTagPosition(){
        setChildTag(mCurrentPosition > mTempDragPosition);
    }

    private void setChildTag(boolean forward){
        if (forward){
            for (int i = mTempDragPosition + 1; i <= mCurrentPosition; i ++){
                mTempView.add(getChildForTag(i)) ;
            }
        }else {
            for (int i = mTempDragPosition - 1; i >= mCurrentPosition; i --){
                mTempView.add(getChildForTag(i)) ;
            }
        }
        resetViewTag(forward) ;
    }

    private void resetViewTag(boolean forward){
        for (int j = 0; j < mTempView.size(); j ++){
            View view = mTempView.get(j) ;
            if (view == null){
                continue;
            }
            int tag = (int) view.getTag();
            tag = forward ? tag - 1 : tag + 1 ;
            view.setTag(tag);
        }
        mTempView.clear();

    }


    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float distanceX, float startY, float distanceY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, distanceX+startX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, distanceY+startY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    private void onDragItem(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - getStatusHeight(mContext);
        if (mWindowLayoutParams.y < mTopBound) {
            mWindowLayoutParams.y = mTopBound;
        }
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams);
        onSwapItem(moveX, moveY);

    }


    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left-(mShadowWidth - (int)(mDensity*1.0f));
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - getStatusHeight(mContext)-(mShadowWidth - (int)(mDensity*1.0f));
        mWindowLayoutParams.alpha = 0.95f;
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mDragImageView.setBackgroundDrawable(new RoundRectDrawableWithShadow(0xffffffff,0, (int)(mDensity*4.0f), (int)(mDensity*8.0f),(int)(mDensity*1.0f)));
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    public void setOnDragListener(OnDragListener listener) {
        mDragListener = listener;
    }

    public static int getStatusHeight(Context context){
        int statusHeight;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public interface OnDragListener {
        void startDrag();

        void stopDrag();
    }
}
