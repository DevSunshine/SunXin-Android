package com.sunxin.plugin.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sunshine.sunxin.BaseFragment;

/**
 * Created by gyzhong on 16/8/24.
 */
public class PersonalFragment extends BaseFragment {
    private FlaxLayout mFlaxLayout ;
    private View mFlaxBg ;
    private TextView mName ;
    private float mHideHeight ;
    private ScrollView mScrollView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_personal_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().addLeftBtn(R.drawable.btn_back, "返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        }) ;

        getTitleView().setTitle("钟光燕") ;
        mHideHeight = getTitleView().setTitleBottomHide();
        getTitleView().hideTitleLine();


        mFlaxLayout = (FlaxLayout) view.findViewById(R.id.id_flax_layout);
        mFlaxBg = view.findViewById(R.id.id_flax_bg);
        mName = (TextView) view.findViewById(R.id.id_title);
        mFlaxLayout.setSmoothListener(new FlaxLayout.SmoothListener() {
            @Override
            public void onSmoothSlide(View changeView, float dy) {

                if (changeView.getTop() == 0){
                    getTitleView().hideTitleLine();
                    getTitleView().setTitleBottomHide();
                }else {
                    if (mName.getTop() + changeView.getTop() < 0){

                    }
                    getTitleView().setTitlePosition(mName.getTop() + changeView.getTop()+mHideHeight);
                    Log.v("zgy","===============getTop============"+Math.abs(mName.getTop() + changeView.getTop()));
                }
            }

            @Override
            public void onSmoothShow(View backgroundView, int orientation) {

                if (orientation == FlaxLayout.UP){
                    getTitleView().hideTitleLine();
                    backgroundView.setBackgroundColor(getResources().getColor(R.color.color333));
                }else {
                    getTitleView().showTitleLine();
                    backgroundView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                Log.v("zgy","===============orientation============"+orientation);
            }
        });



    }
}
