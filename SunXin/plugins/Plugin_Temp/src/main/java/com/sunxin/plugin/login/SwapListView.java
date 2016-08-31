package com.sunxin.plugin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by moon.zhong on 2015/3/18.
 */
public class SwapListView extends ListView {

    /*复位id*/
    private final int INVALID = -1;
    /*移动item的id*/
    private long mMobileItemId;
    /*前一个item的id*/
    private long mAboveItemId;
    /*下一个item的id*/
    private long mBelowItemId;
    /*按下时点的坐标*/
    private int mDownMotionY;
    /*当前点的坐标 Y*/
    private int mCurrentMotionY;
    /*移动的Drawable*/
    private BitmapDrawable mMobileView;
    /*当前Drawable的位置*/
    private Rect mCellCurrentBounds;
    /*按下时item的位置*/
    private Rect mCellOriginBounds;
    /*触摸可活动的点id*/
    private int mActivityPointId = INVALID;
    /*拖动item是否在移动*/
    private boolean isMove = false;
    /*ListView是否在滚动*/
    private boolean isScroll = false;
    /*这个变量比较关键，用来实现ListView的滚动*/
    private int mScrollOffset;
    /*用户数据集合*/
    private List<Object> mList;
    /*每个item执行的动画时间*/
    private final int MOVE_DURATION = 150;
    /*定义了一个属性动画的插值器*/
    private final static TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() {
        @Override
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left,endValue.left,fraction),
                             interpolate(startValue.top,endValue.top,fraction),
                             interpolate(startValue.right,endValue.right,fraction),
                             interpolate(startValue.bottom,endValue.bottom,fraction));
        }
        public int interpolate(int start, int end, float fraction) {
            return (int)(start + fraction * (end - start));
        }
    };
    /*自动滚动ListView的距离*/
    private int mScrollDistance = 2 ;

    public SwapListView(Context context) {
        super(context);
        init(context);
    }

    public SwapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwapListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        /*内部设置滚动监听*/
        setOnScrollListener(new OnScrollerImpl());
        /*长按触发拖动item*/
        setOnItemLongClickListener(new ItemLongClick());
        mScrollDistance = (int) (mScrollDistance*context.getResources().getDisplayMetrics().density);
    }

    private class ItemLongClick implements OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mScrollOffset = 0;
            /*获取Item的View*/
            View mobileView = getViewForPosition(position);
            mobileView.setVisibility(INVISIBLE);
            /*通过View创建一个Drawable*/
            mMobileView = createDrawable(mobileView);
            //获取item的Id，用来获取View
            mMobileItemId = getAdapter().getItemId(position);
            /*更新上下相邻的两个Id值*/
            updateNeighborViewsForID(mMobileItemId);
            /*把拖动标志设为true*/
            isMove = true;
            return true;
        }
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

    /**
     * 根据Id来获取View
     * @param itemID
     * @return
     */
    public View getViewForID(long itemID) {
        int firstVisiblePosition = getFirstVisiblePosition();
        BaseAdapter adapter = ((BaseAdapter) getAdapter());
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    /**
     *
     * @param itemID
     * @return
     */
    public int getPositionForID(long itemID) {
        View v = getViewForID(itemID);
        if (v == null) {
            return -1;
        } else {
            return getPositionForView(v);
        }
    }

    private void updateNeighborViewsForID(long itemID) {
        int position = getPositionForID(itemID);
        BaseAdapter adapter = ((BaseAdapter) getAdapter());
        mAboveItemId = adapter.getItemId(position - 1);
        mBelowItemId = adapter.getItemId(position + 1);
    }

    /**
     * 创建一个
     * BitmapDrawable
     * @param view
     * @return
     */
    private BitmapDrawable createDrawable(View view) {
        BitmapDrawable bitmapDrawable = null;
        /*获取位置变量*/
        int left = view.getLeft();
        int top = view.getTop();
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        /*首先创建一个Bitmap，这个位图为空位图，里面啥也没有*/
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        /*创建一个画布，把空位图放入画布中*/
        Canvas canvas = new Canvas(bitmap);
        /*把所选的item的View 绘制到画布上，其实也就是会知道了Bitmap上*/
        view.draw(canvas);
        /*绘制边矩形*/
        Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()) ;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xff1f8c03);
        canvas.drawRect(rect, paint);
        /*把bitmap转化成BitmapDrawable*/
        bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        /*设置drawable的原始位置*/
        mCellOriginBounds = new Rect(left, top, right, bottom);
        /*初始化drawable的当前位置*/
        mCellCurrentBounds = new Rect(mCellOriginBounds);
        bitmapDrawable.setBounds(mCellCurrentBounds);
        /*设置drawable的透明度*/
        bitmapDrawable.setAlpha(120);
        return bitmapDrawable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                /*这里是获取按下的时候点的坐标位置*/
                int pointIndex = MotionEventCompat.getActionIndex(ev);
                mActivityPointId = MotionEventCompat.getPointerId(ev, pointIndex);
                if (mActivityPointId == INVALID) {
                    break;
                }
                mDownMotionY = (int) MotionEventCompat.getY(ev, pointIndex);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isMove) {
                    int pointMoveIndex = MotionEventCompat.getActionIndex(ev);
                    mActivityPointId = MotionEventCompat.getPointerId(ev, pointMoveIndex);
                    if (mActivityPointId == INVALID) {
                        break;
                    }
                    mCurrentMotionY = (int) MotionEventCompat.getY(ev, mActivityPointId);
                    int delay = mCurrentMotionY - mDownMotionY;
                    /*偏移Drawable的位置*/
                    mCellCurrentBounds.offsetTo(mCellCurrentBounds.left, mCellOriginBounds.top + delay + mScrollOffset);

                    mMobileView.setBounds(mCellCurrentBounds);
                    /*去判断是否需要交换数据*/
                    checkExchangeItem();
                    isScroll = false;
                    /*判断是否需要滚动ListView*/
                    isScroll = checkScroll();
                    /*然后重绘*/
                    invalidate();
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                /*松手执行结束拖拽操作*/
                touchEventEnd();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointUpIndex = MotionEventCompat.getActionIndex(ev);
                int pointId = MotionEventCompat.getPointerId(ev, pointUpIndex);
                if (pointId == mActivityPointId) {
                    /*松手执行结束拖拽操作*/
                    touchEventEnd();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                /*这里直接复原*/
                touchEventCancel();
                break;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        /**
         * 引用数据
         */
        mList = ((BaseAdapter) adapter).getDataList();
    }


    /**
     * 检测是否需要交换数据，如果需要则交换
     */
    private void checkExchangeItem() {
        /*这个如果是初始化的时候调用，则直接返回*/
        if (mCellOriginBounds == null) {
            return;
        }
        /*获取滑动两点之间的差值*/
        final int deltaY = mCurrentMotionY - mDownMotionY;
        /*获取滑动的总值*/
        int deltaYTotal = mCellOriginBounds.top + mScrollOffset + deltaY;
        /*获取相邻两个View 和自己的View*/
        View belowView = getViewForID(mBelowItemId);
        View mobileView = getViewForID(mMobileItemId);
        View aboveView = getViewForID(mAboveItemId);
        /*判断是向下还是向上滑动*/
        int halfBelow = 0 ;
        if (belowView != null){
            halfBelow = (int) (belowView.getHeight() / 2.0f);
        }
        int halfAbove = 0 ;
        if (aboveView != null){
            halfAbove = (int) (aboveView.getHeight() / 2.0f);
        }
        boolean isBelow = (belowView != null) && (deltaYTotal > belowView.getTop() - halfBelow);
        boolean isAbove = (aboveView != null) && (deltaYTotal < aboveView.getTop() + halfAbove);
        if (isAbove || isBelow) {
            final long switchItemID = isBelow ? mBelowItemId : mAboveItemId;
            /*获取下一个 item 的View*/
            View switchView = isBelow ? belowView : aboveView;
            /*获取原始 item 的 position，用于数据交换*/
            final int originalItem = getPositionForView(mobileView);
            /*真正交换两个数据*/
            SwapItem(originalItem, getPositionForView(switchView));
            /*设置显示和隐藏View*/
            switchView.setVisibility(INVISIBLE);
            mobileView.setVisibility(VISIBLE);
            /*更新Adapter 的数据*/
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            /*重新赋值*/
            mDownMotionY = mCurrentMotionY;
            /*更新相邻两个 item 的 id*/
            updateNeighborViewsForID(mMobileItemId);

            /*设置偏移量，这个的作用是用于ListView 的滚动*/
            mScrollOffset = mScrollOffset + deltaY;

            /*--------------------------以下部分为动画效果部分-------------------------*/

            View swappedView = getViewForID(switchItemID) ;
            int switchViewNewTop = swappedView.getTop();
            int switchTop = switchView.getTop() ;
            int delta = switchTop - switchViewNewTop ;

            swappedView.setTranslationY(delta);
            ObjectAnimator animator = ObjectAnimator.ofFloat(swappedView,
                    "translationY",0);
            animator.setDuration(MOVE_DURATION);
            animator.start();

        }
    }

    /**
     * 根据位置 交换两个数据，
     *
     * @param indexOne
     * @param indexTwo
     */
    private void SwapItem(int indexOne, int indexTwo) {
        Object temp = mList.get(indexOne);
        mList.set(indexOne, mList.get(indexTwo));
        mList.set(indexTwo, temp);
    }

    /**
     *
     */
    private void touchEventEnd() {
        final View mobileView = getViewForID(mMobileItemId);
        if (isMove){
            isMove = false;
            isScroll = false;
            mActivityPointId = INVALID;

            /*------------放开手指时的结束动画-------------*/
            mCellCurrentBounds.offsetTo(mCellOriginBounds.left, mobileView.getTop());

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
                    mAboveItemId = INVALID;
                    mMobileItemId = INVALID;
                    mBelowItemId = INVALID;
                    mobileView.setVisibility(VISIBLE);
                    mMobileView = null;
                    setEnabled(true);
                    invalidate();
                }
            });
            hoverViewAnimator.start();
        }else {
            touchEventCancel();
        }
    }


    /**
     * 检测是否可以滚懂
     * @return
     */
    private boolean checkScroll() {

        int height = getHeight();
        /*如果当前 item 的顶部小于0，即到了 listView 的顶部不可见的位置。*/
        if (mCellCurrentBounds.top < 0)  {
            smoothScrollBy(-4, 0);
            return true;
        }
        /*当当前 item 的底部大于ListView 的高度，即到了 listView 的低部不可见的位置*/
        if (mCellCurrentBounds.top + mCellCurrentBounds.height() > height ) {
            smoothScrollBy(4, 0);
            return true;
        }
        return false;
    }

    private void touchEventCancel() {
        if (isMove) {
            View view = getViewForID(mMobileItemId);
            view.setVisibility(VISIBLE);
            mMobileView = null;
            invalidate();
        }
        isMove = false;
        isScroll = false ;
        mActivityPointId = INVALID;
    }

    /*绘制Drawable*/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mMobileView != null) {
            mMobileView.draw(canvas);
        }
    }


    private class OnScrollerImpl implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /*停止滚动，继续判断是否符合滚动的条件*/
            if (isMove && isScroll) {
                /**/
                isScroll = checkScroll();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (isMove) {
                /*检测和交换 item*/
                checkExchangeItem();
                /*更新相邻的两个 itemId*/
                updateNeighborViewsForID(mMobileItemId);
            }

        }
    }
}
