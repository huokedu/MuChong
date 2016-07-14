package com.htlc.muchong.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;

/**
 * Created by sks on 2015/12/29.
 * 主页四个Fragment的父类
 */
public abstract class HomeFragment extends BaseFragment {
    public CharSequence mTitle;//Fragment的标题
    public int mIconResId;//Fragment的图标资源id

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        ((BaseActivity)getActivity()).mTitleTextView.setText(mTitle);
    }

    public static <T extends HomeFragment> T newInstance(Class<T> clazz, String title, int iconResId) {
        try {
            T fragment = clazz.newInstance();
            fragment.mTitle = title;
            fragment.mIconResId = iconResId;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public View getTabView(Context context) {
        ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.tab_layout_item, null);
        view.setImageResource(mIconResId);
        return view;
    }

}
