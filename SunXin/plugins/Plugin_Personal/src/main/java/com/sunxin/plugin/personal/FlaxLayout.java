package com.sunxin.plugin.personal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Created by gyzhong on 16/8/27.
 * 伸缩布局，让任何控件都能向下和向上拉伸
 * 类似IOS 的 listView 布局滑到低效果
 * 这个控件设计初衷是，用来玩一玩的，
 * 没什么实质性用处，纯属闲的蛋疼！
 */
public class FlaxLayout extends ViewGroup {

    private static final String TAG = "FlaxLayout";
    /**
     * 正在展示内容的布局
     */
    private View mContentView;
    /**
     * 里层布局，一般不用来做内容展示，
     * 主要用来做特效，比如下拉刷新效果等等
     */
    private View mBackgroundView;

    /**
     * 拖动和手势帮助类，主要是用于简化代码用的
     */
    private ViewDragHelper mDragHelper;

    private boolean mIsUnableToDrag;

    private float mInitialMotionX;
    private float mInitialMotionY;

    private boolean mFirstLayout = true;

    public FlaxLayout(Context context) {
        this(context, null);
    }

    public FlaxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlaxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlaxLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final float density = context.getResources().getDisplayMetrics().density;

        final ViewConfiguration viewConfig = ViewConfiguration.get(context);

        setWillNotDraw(false);

        ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Consume all of the space if specified
                // or pick a magic number from thin air otherwise.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (widthMode == MeasureSpec.AT_MOST) {
                    widthMode = MeasureSpec.EXACTLY;
                } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                    widthMode = MeasureSpec.EXACTLY;
                    widthSize = 300;
                }
            } else {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Pick a magic number from thin air instead.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.AT_MOST;
                    heightSize = 300;
                }
            } else {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            }
        }
        setMeasuredDimension(widthSize, heightSize);
        final int childCount = getChildCount();

        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
            throw new RuntimeException("More than two child views are not supported");
        }

        mBackgroundView = null;
        mContentView = null;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                int test = MeasureSpec.makeMeasureSpec(300,MeasureSpec.EXACTLY) ;
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (isBackgroundView(child)) {
                final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        lp.leftMargin + lp.rightMargin,
                        lp.width);
                final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin,
                        lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            }
        }

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (w != oldw) {
            mFirstLayout = true;
        }
    }
    boolean isContentView(View child) {

        return getChildAt(1) == child;
    }

    boolean isBackgroundView(View child) {

        return getChildAt(0) == child;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {

            } else if (isBackgroundView(child)) {
//                child.layout(lp.leftMargin, lp.topMargin,
//                        lp.leftMargin + child.getMeasuredWidth(),
//                        lp.topMargin + child.getMeasuredHeight());
            }
            child.layout(lp.leftMargin, lp.topMargin,
                    lp.leftMargin + child.getMeasuredWidth(),
                    lp.topMargin + child.getMeasuredHeight());

        }
        mFirstLayout = false;
        setAllChildrenVisible();
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);


        if ((mIsUnableToDrag && action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mIsUnableToDrag = false;
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;

                if (mDragHelper.isViewUnder(mContentView, (int) x, (int) y)) {
                    interceptTap = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                if (adx > slop && ady > adx) {
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
            }
        }

        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);

        return interceptForDrag || interceptTap;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        boolean wantTouchEvents = true;

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                final View touchedView = mDragHelper.findTopChildUnder((int) x, (int) y);
                if (dx * dx + dy * dy < slop * slop && touchedView != null) {
                    restoreView() ;
                    break;
                }
                break;
            }
        }

        return wantTouchEvents;
    }

    /**
     * View  恢复原位
     */
    private void restoreView(){

    }

    private void setContentViewOffset(View contentView ,int offset){

    }

    //    @Override
//    public void computeScroll() {
//        final int childCount = getChildCount();
//        float scrimOpacity = 0;
//        for (int i = 0; i < childCount; i++) {
//            final float onscreen = ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen;
//            scrimOpacity = Math.max(scrimOpacity, onscreen);
//        }
//        mScrimOpacity = scrimOpacity;
//
//        // "|" used on purpose; both need to run.
//        if (mLeftDragger.continueSettling(true) | mRightDragger.continueSettling(true)) {
//            ViewCompat.postInvalidateOnAnimation(this);
//        }
//    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
//
//        for (int i = 0, count = mPostedRunnables.size(); i < count; i++) {
//            final DisableLayerRunnable dlr = mPostedRunnables.get(i);
//            dlr.run();
//        }
//        mPostedRunnables.clear();
    }
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams
                ? new LayoutParams((LayoutParams) p)
                : p instanceof ViewGroup.MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{
                android.R.attr.layout_weight
        };

        /**
         * The weighted proportion of how much of the leftover space
         * this child should consume after measurement.
         */
        public float weight = 0;

        /**
         * True if this pane is the slideable pane in the layout.
         */
        boolean slideable;

        /**
         * True if this view should be drawn dimmed
         * when it's been offset from its default position.
         */
        boolean dimWhenOffset;

        Paint dimPaint;

        public LayoutParams() {
            super(FILL_PARENT, FILL_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.weight = source.weight;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, 0);
            a.recycle();
        }

    }

    /**
     * draghelper 回调接口处理
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            Log.v("zgy","=========tryCaptureView========"+view) ;
            if (isBackgroundView(view)){
                return false ;
            }
            if (mIsUnableToDrag) {
                return true;
            }
            return true ;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            int offset = top ;
            Log.v("zgy","=========onViewPositionChanged========"+top) ;
            setContentViewOffset(mContentView, offset);
            invalidate();
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            setAllChildrenVisible();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return isContentView(child) ? child.getWidth() : 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.v("zgy","=========clampViewPositionHorizontal========"+dx) ;
            return child.getLeft();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.v("zgy","=========clampViewPositionHorizontal========"+dy) ;
            int offset = 0 ;
            if (top < 0){
                
            }
            final int width = getHeight();
            return Math.max(-child.getHeight(), Math.min(top, width));
//            return 2;
        }
    }
}
