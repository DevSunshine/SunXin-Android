package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class DragMutilLayout extends RelativeLayout implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
        DragScrollerView.OnScrollerListener{

    private final static TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() {
        @Override
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left, fraction),
                    interpolate(startValue.top, endValue.top, fraction),
                    interpolate(startValue.right, endValue.right, fraction),
                    interpolate(startValue.bottom, endValue.bottom, fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int) (start + fraction * (end - start));
        }
    };
    private final int INVALID = -1;
    private LinearLayout mAnimLayout;
    private MaxGridView[] maxGridViews;
    private GridAdapter[] mGridAdapters;
    private Rect mCellOriginBounds;
    private Rect mCellCurrentBounds;
    private BitmapDrawable mMobileView;
    private boolean isMove;
    private boolean isScroll;
    private int mDownInitX;
    private int mDownInitY;
    private int mCurrentMotionX;
    private int mCurrentMotionY;
    private int mActivityPointId = INVALID;
    private View mHideView;

    private int mCurrentPosition;
    private int mCurrentGridView;
    private int mNextPosition;
    private int mNextGridView;
    private boolean mIsAnimater;

    private DragScrollerView mParentView;

    public DragMutilLayout(Context context) {
        super(context);
    }

    public DragMutilLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragMutilLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragMutilLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof MaxGridView) {
                MaxGridView gridView = (MaxGridView) childView;
                if (maxGridViews == null) {
                    maxGridViews = new MaxGridView[1];
                } else {
                    MaxGridView[] newMaxGridViews = new MaxGridView[maxGridViews.length + 1];
                    System.arraycopy(maxGridViews, 0, newMaxGridViews, 0, maxGridViews.length);
                    maxGridViews = newMaxGridViews;
                }
                gridView.setOnItemClickListener(this);
                gridView.setOnItemLongClickListener(this);
                maxGridViews[maxGridViews.length - 1] = gridView;
                gridView.setOnAnimaterListener(new MaxGridView.OnAnimaterListener() {
                    @Override
                    public void onAnimaterStart() {
                        mIsAnimater = true;
                    }

                    @Override
                    public void onAnimaterend() {
                        mIsAnimater = false;
                    }
                });
            }
        }
        if (maxGridViews != null) {
            mGridAdapters = new GridAdapter[maxGridViews.length];
        }

        for (int j = 0; j < maxGridViews.length; j++) {
            List<String> data = new ArrayList<>();
            mGridAdapters[j] = new GridAdapter(data);
            maxGridViews[j].setAdapter(mGridAdapters[j]);
        }


        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                View parent = (View) getParent();
                if (parent instanceof ScrollView) {
                    mParentView = (DragScrollerView) getParent();
                    mParentView.setOnScrollerListener(DragMutilLayout.this);
                }
                return true;
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        View mobileView = getViewForPosition(adapterView, position);
        mobileView.setVisibility(INVISIBLE);
//        mHideView = mobileView;
        mMobileView = createDrawable(adapterView, mobileView);
        isMove = true;
        mCurrentPosition = position;
        MaxGridView maxGridView = (MaxGridView) adapterView;
        for (int i = 0; i < maxGridViews.length; i++) {
            if (maxGridView == maxGridViews[i]) {
                mCurrentGridView = i;
            }
        }
        if (mParentView != null) {
            mParentView.requestDisallowInterceptTouchEvent(true);
        }
        invalidate();
        return true;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mMobileView != null) {
            mMobileView.draw(canvas);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                int pointIndex = MotionEventCompat.getActionIndex(event);
                mActivityPointId = MotionEventCompat.getPointerId(event, pointIndex);
                if (mActivityPointId == INVALID) {
                    break;
                }
                mDownInitY = (int) MotionEventCompat.getY(event, pointIndex);
                mDownInitX = (int) MotionEventCompat.getX(event, pointIndex);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        if (isMove) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                int pointIndex = MotionEventCompat.getActionIndex(event);
                mActivityPointId = MotionEventCompat.getPointerId(event, pointIndex);
                if (mActivityPointId == INVALID) {
                    break;
                }
                mDownInitY = (int) MotionEventCompat.getY(event, pointIndex);
                mDownInitX = (int) MotionEventCompat.getX(event, pointIndex);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isMove) {
                    int pointMoveIndex = MotionEventCompat.getActionIndex(event);
                    mActivityPointId = MotionEventCompat.getPointerId(event, pointMoveIndex);
                    if (mActivityPointId == INVALID) {
                        break;
                    }
                    mCurrentMotionY = (int) MotionEventCompat.getY(event, mActivityPointId);
                    int delayY = mCurrentMotionY - mDownInitY;
                    mCurrentMotionX = (int) MotionEventCompat.getX(event, mActivityPointId);
                    int delayX = mCurrentMotionX - mDownInitX;
                    mCellCurrentBounds.offsetTo(mCellOriginBounds.left + delayX, mCellOriginBounds.top + delayY);

                    mMobileView.setBounds(mCellCurrentBounds);
                    checkExchangeItem();
                    isScroll = checkScroll() ;
                    invalidate();
                    return false;
                } else {
                    int pointIndexs = MotionEventCompat.getActionIndex(event);
                    mActivityPointId = MotionEventCompat.getPointerId(event, pointIndexs);
                    if (mActivityPointId == INVALID) {
                        break;
                    }
                    mDownInitY = (int) MotionEventCompat.getY(event, pointIndexs);
                    mDownInitX = (int) MotionEventCompat.getX(event, pointIndexs);
                }
                break;
            case MotionEvent.ACTION_UP:
//                touchEventEnd();
                touchEventCancel();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointUpIndex = MotionEventCompat.getActionIndex(event);
                int pointId = MotionEventCompat.getPointerId(event, pointUpIndex);
                if (pointId == mActivityPointId) {
//                    touchEventEnd();
                    touchEventCancel();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void touchEventCancel() {
//        if (isMove) {
//            if (mHideView != null) {
//                mHideView.setVisibility(VISIBLE);
//                mHideView = null;
//            }
//
//        }
        isMove = false;
        isScroll = false;
        mActivityPointId = INVALID;
        if (mParentView != null) {
            mParentView.requestDisallowInterceptTouchEvent(false);
        }
        createUnTouchAnimation();
    }

    private void getHideView() {

    }

    private void checkExchangeItem() {

        mNextPosition = pointToPosition(mCellCurrentBounds.centerX(), mCellCurrentBounds.centerY());

        if (mNextPosition != AbsListView.INVALID_POSITION && (mCurrentGridView != mNextGridView || mNextPosition != mCurrentPosition) && !mIsAnimater) {
            Log.v("zgy", "================mCurrentPosition====" + mCurrentPosition + ",=========mNextPosition=" + mNextPosition);
            if (mCurrentGridView != mNextGridView) {//跨控件

                if (mNextGridView > mCurrentGridView) {//向下

                    int count = mGridAdapters[mCurrentGridView].getCount();
                    swapItem(mGridAdapters[mCurrentGridView], mCurrentPosition, count - 1);

                    String tempNext = mGridAdapters[mNextGridView].getItem(0);
                    String tempCurrent = mGridAdapters[mCurrentGridView].getItem(count - 1);
                    mGridAdapters[mCurrentGridView].getDataList().set(count - 1, tempNext);

                    mGridAdapters[mCurrentGridView].hidePosition(count - 1);
                    maxGridViews[mCurrentGridView].animateReorder(mCurrentPosition, count - 1);

                    createAnimater(mNextGridView, 0, mCurrentGridView, count - 1);

                    mGridAdapters[mNextGridView].hidePosition(0);

                    mGridAdapters[mNextGridView].getDataList().set(0, tempCurrent);
                    if (mNextPosition > 0) {
                        swapItem(mGridAdapters[mNextGridView], 0, mNextPosition);
                        mGridAdapters[mNextGridView].hidePosition(mNextPosition);
                        maxGridViews[mNextGridView].animateReorder(0, mNextPosition);
                    }


                } else {//向上
                    int nextCount = mGridAdapters[mNextGridView].getCount();
                    int currentCount = mGridAdapters[mCurrentGridView].getCount();


                    //内部交换
                    swapItem(mGridAdapters[mCurrentGridView], mCurrentPosition, 0);

                    String tempNext = mGridAdapters[mNextGridView].getItem(nextCount - 1);
                    String tempCurrent = mGridAdapters[mCurrentGridView].getItem(0);
                    mGridAdapters[mCurrentGridView].getDataList().set(0, tempNext);

                    mGridAdapters[mCurrentGridView].hidePosition(0);
                    maxGridViews[mCurrentGridView].animateReorder(mCurrentPosition, 0);


                    createAnimater(mNextGridView, nextCount - 1, mCurrentGridView, 0);


                    mGridAdapters[mNextGridView].hidePosition(nextCount - 1);

                    mGridAdapters[mNextGridView].getDataList().set(nextCount - 1, tempCurrent);
                    if (mNextPosition < nextCount - 1) {

                        swapItem(mGridAdapters[mNextGridView], nextCount - 1, mNextPosition);
                        mGridAdapters[mNextGridView].hidePosition(mNextPosition);
                        maxGridViews[mNextGridView].animateReorder(nextCount - 1, mNextPosition);
                    }

                    Log.v("zgy", "================tempCurrent=====" + tempCurrent);


                }
                mCurrentGridView = mNextGridView;
            } else {
                swapItem(mGridAdapters[mCurrentGridView], mCurrentPosition, mNextPosition);
                mGridAdapters[mCurrentGridView].hidePosition(mNextPosition);
                maxGridViews[mCurrentGridView].animateReorder(mCurrentPosition, mNextPosition);
            }
            mCurrentPosition = mNextPosition;


        }
    }

    public void swapItem(GridAdapter adapter, int oldPosition, int newPosition) {
        String temp = adapter.getDataList().get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(adapter.getDataList(), i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(adapter.getDataList(), i, i - 1);
            }
        }
        adapter.getDataList().set(newPosition, temp);
        adapter.notifyDataSetChanged();
    }


    public View getViewForPosition(ViewGroup group, int position) {
        View view = group.getChildAt(position);
        return view;
    }

    private void swapItem(List<String> list, int indexOne, List<String> listTow, int indexTwo) {
        String tempOne = list.get(indexOne);
        String tempTwo = listTow.get(indexTwo);
        list.set(indexTwo, tempTwo);
        listTow.set(indexOne, tempOne);
    }

    public int pointToPosition(int moveX, int moveY) {
        for (int i = 0; i < maxGridViews.length; i++) {
            int position = maxGridViews[i].pointToPosition(moveX, moveY - maxGridViews[i].getTop());
            if (position != AbsListView.INVALID_POSITION) {
                mNextGridView = i;
                return position;

            }
        }

        return AbsListView.INVALID_POSITION;
    }

    private BitmapDrawable createDrawable(ViewGroup group, View view) {
        BitmapDrawable bitmapDrawable;
        int left = view.getLeft() + group.getLeft();
        int top = view.getTop() + group.getTop();
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorMain));
        canvas.drawRect(rect, paint);
        bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        mCellOriginBounds = new Rect(left, top, right, bottom);
        mCellCurrentBounds = new Rect(mCellOriginBounds);
        bitmapDrawable.setBounds(mCellCurrentBounds);
        bitmapDrawable.setAlpha(250);
        return bitmapDrawable;
    }

    public void setData(List<String> data) {
        if (data != null) {
            if (data.size() > 3) {
                mGridAdapters[0].getDataList().clear();
                mGridAdapters[0].getDataList().addAll(data.subList(0, 3));
                mGridAdapters[0].notifyDataSetChanged();
                mGridAdapters[1].getDataList().clear();
                mGridAdapters[1].getDataList().addAll(data.subList(3, data.size()));
                mGridAdapters[1].notifyDataSetChanged();
            } else {
                mGridAdapters[0].getDataList().clear();
                mGridAdapters[0].getDataList().addAll(data.subList(0, data.size()));
                mGridAdapters[0].notifyDataSetChanged();
            }
        }
    }

    /**
     * 创建动画层
     */
    private void setUpAnimLayout(int height) {
        if (mAnimLayout != null){
            if (mAnimLayout.getHeight()>=height){
                return;
            }
        }
        removeView(mAnimLayout);
        mAnimLayout = null;
        mAnimLayout = new LinearLayout(getContext());
        addView(mAnimLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

    }

    /**
     * 创建跨空间动画
     */
    private void createAnimater(final int nextGridview, final int nextPosition, final int currentGridview, final int currentPosition) {


        MaxGridView gridViews = maxGridViews[nextGridview];
        final MaxGridView gridViewe = maxGridViews[currentGridview];
        View views = getViewForPosition(gridViews, nextPosition);
        View viewe = getViewForPosition(gridViewe, currentPosition);

        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Bitmap bitmap = createBitmap(views);
        imageView.setLayoutParams(lp);
        imageView.setImageBitmap(bitmap);
        int height = Math.max(maxGridViews[nextGridview].getTop() + views.getHeight(),
                maxGridViews[currentGridview].getTop() + viewe.getHeight());
        setUpAnimLayout(height);
        mAnimLayout.addView(imageView);
        float scaleX = viewe.getWidth() * 1.0f / views.getWidth();
        float scaleY = viewe.getHeight() * 1.0f / views.getHeight();
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        AnimatorSet animatorSet = createTranslationAnimations(imageView, views.getLeft() + gridViews.getLeft(),
                viewe.getLeft() + gridViewe.getLeft(), views.getTop() + gridViews.getTop(),
                viewe.getTop() + gridViewe.getTop(), scaleX, scaleY);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimLayout.removeAllViews();
                mGridAdapters[currentGridview].hidePosition(-1);

            }
        });
    }

    private Bitmap createBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY,
                                                    float endY, float scaleX, float scaleY) {
        Log.v("zgy", "===============startX==" + startX + "=========endX=" + endX + "===scaleY=" + scaleY + "===scaleX=" + scaleX);
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        ObjectAnimator scaleXA = ObjectAnimator.ofFloat(view, "scaleX",
                1.0f, scaleX);
        ObjectAnimator scaleYA = ObjectAnimator.ofFloat(view, "scaleY",
                1.0f, scaleY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY, scaleXA, scaleYA);
        return animSetXY;
    }

    private void createUnTouchAnimation() {
        Log.v("zgy", "===================mCurrentGridView====" + mCurrentGridView + "===mCurrentPosition=" + mCurrentPosition);
        View view = getViewForPosition(maxGridViews[mCurrentGridView], mCurrentPosition);
        if (view == null) {
            mMobileView = null;
            invalidate();
            mGridAdapters[mCurrentGridView].hidePosition(-1);
            return;
        }
        mCellCurrentBounds.offsetTo(view.getLeft() + maxGridViews[mCurrentGridView].getLeft(),
                view.getTop() + maxGridViews[mCurrentGridView].getTop());
        ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(mMobileView, "bounds",
                sBoundEvaluator, mCellCurrentBounds);
        hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setEnabled(true);
                mMobileView = null;
                invalidate();
                mGridAdapters[mCurrentGridView].hidePosition(-1);
            }
        });
        hoverViewAnimator.setDuration(200);
        hoverViewAnimator.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemPosition = 0;
        MaxGridView maxGridView = (MaxGridView) parent;
        for (int i = 0; i < maxGridViews.length; i++) {
            if (maxGridView == maxGridViews[i]) {
                itemPosition = i;
            }
        }
        Toast.makeText(getContext().getApplicationContext(), "点击了第" + (itemPosition + 1) + "个GridView的第" + (position + 1) + "个位置", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollY(int y) {
//        if (isMove && isScroll){
//            isScroll = checkScroll();
//            checkExchangeItem();
//        }
        Log.v("ZGY","==============mParentView======"+mParentView.getScollerHeight()) ;
    }

    private static class WrapperView {
        private View mTarget;

        public WrapperView(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }
    }

    private boolean checkScroll() {

        if (mParentView != null){

            int height = mParentView.getHeight()+mParentView.getScrollY();
            Log.v("zgy","==========height==========="+height+"============mCellCurrentBounds="+(mCellCurrentBounds.top + mCellCurrentBounds.height())) ;
            if (mCellCurrentBounds.top < mParentView.getScrollY() && mParentView != null)  {
                mParentView.smoothScrollBy(0, -4);
                return true;
            }
            if (mCellCurrentBounds.top + mCellCurrentBounds.height() > height ) {
                mParentView.smoothScrollBy(0, 4);
                return true;
            }
        }

        return false;
    }
}
