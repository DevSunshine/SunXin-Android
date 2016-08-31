package com.sunxin.plugin.login;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by moon.zhong on 2015/3/18.
 */
public class StringAdapter extends BaseAdapter<StringAdapter.ViewHolder,String>{

    public StringAdapter(List<String> list) {
        super(list);
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater inflater, int position) {
        final float density = inflater.getContext().getResources().getDisplayMetrics().density ;
        TextView textView = new TextView(inflater.getContext()) ;
        int padding = (int) (15*density+ 0.5f);
        textView.setPadding(padding,padding,padding,padding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        ViewHolder viewHolder = new ViewHolder(textView) ;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(getItem(position));
    }

    protected class ViewHolder extends BaseAdapter.BaseViewHolder{
        public TextView textView ;
        protected ViewHolder(View mView) {
            super(mView);
            textView = (TextView) mView;
            textView.setBackgroundColor(0xffffffff);
        }
    }
}
