package com.sunshine.sunxin.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.msg.MsgMenuItem;

import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/30.
 * e-mail guangyanzhong@163.com
 */

public class TabMsgPopView extends PopupWindow {

    Configuration mConfiguration;
    onItemClickListener mListener;
    private RecyclerView mRecyclerView;
    Window mWindow;

    public TabMsgPopView(Context context) {
        super(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.msg_menu_layout, null);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.id_msg_menu_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(manager);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.MenuAnimation);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        update();
        setOutsideTouchable(true);
        if (context instanceof Activity) {
            mWindow = ((Activity) context).getWindow();
        }

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                windowAlpha(false);
            }
        });

    }

    public void show(View targetView, onItemClickListener listener) {
         showAsDropDown(targetView, 0, mConfiguration.yoff, Gravity.RIGHT);
        this.mListener = listener;
        windowAlpha(true);
    }

    private void windowAlpha(boolean show) {
        if (mWindow != null) {
            float start = show ? 0 : 1 ;
            float end = show ? 1 : 0 ;
            ValueAnimator animator = ValueAnimator.ofFloat(start, end);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue()*0.25f;
                    float alpha = 1.0f - value ;
                    WindowManager.LayoutParams lp = mWindow.getAttributes();
                    lp.alpha = alpha;
                    mWindow.setAttributes(lp);
                }
            });
            animator.setDuration(220) ;
            animator.start();
        }
    }

    public interface onItemClickListener {
        void clickItem(int position);
    }

    public TabMsgPopView setConfiguration(Configuration configuration) {
        if (mConfiguration == null) {
            mConfiguration = new Configuration(configuration);
        } else {
            mConfiguration.deepCopy(configuration);
        }
        return this;
    }


    public static class Configuration {
        int xoff;
        int yoff;

        public Configuration(int x, int y) {
            xoff = x;
            yoff = y;

        }

        public Configuration(Configuration configuration) {
            this.xoff = configuration.xoff;
            this.yoff = configuration.yoff;
        }

        public void deepCopy(Configuration configuration) {
            this.xoff = configuration.xoff;
            this.yoff = configuration.yoff;
        }

    }

    public TabMsgPopView setMenuItems(List<MsgMenuItem> menuItems) {
        mRecyclerView.setAdapter(new MsgMenuAdapter(menuItems));
        return this;
    }

    public class MsgMenuAdapter extends RecyclerView.Adapter<MsgMenuAdapter.MsgMenuViewHolder> {

        private List<MsgMenuItem> mMenus;

        public MsgMenuAdapter(List<MsgMenuItem> mMenus) {
            this.mMenus = mMenus;
        }

        @Override
        public MsgMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MsgMenuViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_menu_item, parent, false));
        }

        @Override
        public int getItemCount() {
            return mMenus == null ? 0 : mMenus.size();
        }

        @Override
        public void onBindViewHolder(MsgMenuViewHolder holder, final int position) {

            holder.imageView.setImageResource(mMenus.get(position).resId);
            holder.textView.setText(mMenus.get(position).title);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.clickItem(position);
                }
            });
        }

        public class MsgMenuViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView textView;

            public MsgMenuViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.id_menu_icon);
                textView = (TextView) itemView.findViewById(R.id.id_menu_text);
            }

        }
    }

}
