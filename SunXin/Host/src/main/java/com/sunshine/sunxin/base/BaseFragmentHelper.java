package com.sunshine.sunxin.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 *
 */
public abstract class BaseFragmentHelper {

    private FragmentManager mFragmentManager;
    private int mContainerId;
    private int mCurrentItem;
    private OnFragmentFetchListener mFragmentFetchListener;
    private OnFragmentInitListener mOnFragmentInitListener;

    public BaseFragmentHelper(FragmentManager mFragmentManager, int mContainerId) {
        this.mFragmentManager = mFragmentManager;
        this.mContainerId = mContainerId;
    }

    private void hideFragment() {
        BaseFragment fragment = getFragment(mCurrentItem);
        if (fragment.isAdded() && !fragment.isHidden()) {
            this.mFragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    private void fetchFragment() {
        BaseFragment fragment = getFragment(mCurrentItem);
        if (!fragment.isAdded()) {
            this.mFragmentManager.beginTransaction().add(mContainerId, fragment).commit();
            if (mOnFragmentInitListener != null) {
                mOnFragmentInitListener.onFragmentAdd(mCurrentItem);
            }
        } else {
            this.mFragmentManager.beginTransaction().show(fragment).commit();
        }
        if (mFragmentFetchListener != null) {
            mFragmentFetchListener.onFragmentSelected(mCurrentItem);
        }
        fragment.fragmentSelect();
    }

    public abstract <T extends Fragment> T getFragment(int position);

    public int getCurrentItem() {
        return mCurrentItem;
    }


    public void setCurrentItem(int position) {

        hideFragment();
        mCurrentItem = position;
        fetchFragment();
    }

    public abstract int getCount();

    public void setOnFragmentFetchListener(OnFragmentFetchListener listener) {
        mFragmentFetchListener = listener;
    }

    public void setOnFragmentInitListener(OnFragmentInitListener listener) {
        mOnFragmentInitListener = listener;
    }

    public interface OnFragmentFetchListener {

        void onFragmentSelected(int position);

    }

    public interface OnFragmentInitListener {

        void onFragmentAdd(int position);
    }
}
