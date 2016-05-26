package com.htlc.muchong.activity;

import android.view.View;
import android.widget.ListView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.ShoppingCartAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.FirstFragment;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sks on 2016/5/26.
 */
public class ShoppingCartActivity extends BaseActivity{

    private ListView listView;
    private ShoppingCartAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_shopping_cart);
        mTitleRightTextView.setText(R.string.delete);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "删除。。。。。。。");
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ShoppingCartAdapter();
        listView.setAdapter(adapter);

        initData();
    }

    @Override
    protected void initData() {
        List<String> strings = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        adapter.setData(strings,false);
    }
}
