package com.htlc.muchong.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.BannerFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sks on 2016/5/23.
 */
public class PaiDetailActivity extends BaseActivity{
    protected BannerFragment mBannerFragment;
    private TextView textButton;

    protected ListView mCommentListView;
    private CommentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_detail;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_pai_detail);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });
        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ToastUtil.showToast(App.app, "banner position = " + position);
            }
        });


        textButton = (TextView) findViewById(R.id.textButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips();
            }
        });

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);
        initData();
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogAppCompat);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.pai_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void initData() {
        mBannerFragment.setData(Arrays.asList(FirstFragment.sampleNetworkImageURLs));
        List<String> strings = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        adapter.setData(strings,false);
    }

}
