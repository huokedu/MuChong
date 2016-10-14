package com.htlc.muchong.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.BrowserActivity;
import com.htlc.muchong.activity.LoginActivity;
import com.htlc.muchong.activity.MerchantOrderListActivity;
import com.htlc.muchong.activity.MyJianListActivity;
import com.htlc.muchong.activity.MyMessageListActivity;
import com.htlc.muchong.activity.MyPaiListActivity;
import com.htlc.muchong.activity.MyPostListActivity;
import com.htlc.muchong.activity.OrderListActivity;
import com.htlc.muchong.activity.PayActivity;
import com.htlc.muchong.activity.SettingActivity;
import com.htlc.muchong.activity.ShoppingCartActivity;
import com.htlc.muchong.activity.TaLikeActivity;
import com.htlc.muchong.adapter.FifthRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LogUtils;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;

import model.UserInfoBean;

/**
 * Created by sks on 2016/1/27.
 * 个人中心Fragment
 */
public class FifthFragment extends HomeFragment {
    private FifthRecyclerViewAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fifth;
    }

    @Override
    protected void setupView() {
        LogUtils.e("setupView---",""+App.app.isLogin());
        mRecyclerView = findViewById(R.id.recyclerView);
        adapter = new FifthRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(getContext(), 40);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == adapter.getItemCount() - 1) {
                    outRect.bottom = space;
                }
            }
        });
        adapter.setOnItemClickListener(new RecyclerViewOnItemClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("onStart---",""+App.app.isLogin());
        if (App.app.isLogin()) {
            initData();
        } else {
            adapter.setUserInfoBean(null);
        }

    }

    @Override
    protected void initData() {
        LogUtils.e("initData---",""+App.app.isLogin());
        adapter.setUserInfoBean(LoginUtil.getUserInfo());
        BaseActivity activity = (BaseActivity) getActivity();
        App.app.appAction.getUserInfo(activity.new BaseActionCallbackListener<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean data) {
                LoginUtil.setUserInfo(data);
                adapter.setUserInfoBean(LoginUtil.getUserInfo());
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {

            }
        });

    }

    /*根据点击条目的位置，跳转对应的界面*/
    private class RecyclerViewOnItemClickListener implements FifthRecyclerViewAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            switch (position) {
                case 0:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), PayActivity.class));
                    return;
                case 1:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                    return;
                case 2:
                    if (!App.app.isLogin()) break;
                    TaLikeActivity.goTaLikeActivity(getContext(), LoginUtil.getUser().id);
                    return;
                case 3:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), MyJianListActivity.class));
                    return;
                case 4:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), OrderListActivity.class));
                    return;
                case 5:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), MyPaiListActivity.class));
                    return;
                case 6:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), MyPostListActivity.class));
                    return;
                case 7:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), MyMessageListActivity.class));
                    return;
                case 8:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    return;
                case 9:
                    if (!App.app.isLogin()) break;
                    Intent intent = new Intent(getActivity(), BrowserActivity.class);
                    intent.putExtra("url","http://t15.damaimob.com/Home/HomeRefund/index.html");
                    startActivity(intent);
                    return;
                case 10:
                    if (!App.app.isLogin()) break;
                    startActivity(new Intent(getActivity(), MerchantOrderListActivity.class));
                    return;
                default:
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
            }
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
