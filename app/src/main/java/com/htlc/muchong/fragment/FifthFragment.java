package com.htlc.muchong.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.LoginActivity;
import com.htlc.muchong.adapter.FifthRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

/**
 * Created by sks on 2016/1/27.
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

        mRecyclerView = findViewById(R.id.recyclerView);
        adapter = new FifthRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(getContext(),40);
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if(parent.getChildAdapterPosition(view) == adapter.getItemCount()-1){
                    outRect.bottom = space;
                }
            }
        });
        adapter.setOnItemClickListener(new FifthRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showToast(App.app, "mRecyclerView position " + position);
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


    }

    @Override
    protected void initData() {
        adapter.setMoney(100);
    }
}
