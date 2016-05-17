package com.htlc.muchong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sks on 2016/5/13.
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(),null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    /**
     * 查找view
     */
    protected <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }
    /**
     * 设置布局文件id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void setupView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
