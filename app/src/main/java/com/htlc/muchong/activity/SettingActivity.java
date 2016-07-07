package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.DataCleanManager;
import com.larno.util.ToastUtil;

import java.io.File;

/**
 * Created by sks on 2016/5/27.
 * 个人中心---设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView textLogout;
    private long cacheSize;
    private long externalCacheSize;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_setting);

        RelativeLayout relativeFeedback = (RelativeLayout) findViewById(R.id.relativeFeedback);
        RelativeLayout relativeCache = (RelativeLayout) findViewById(R.id.relativeCache);
        relativeFeedback.setOnClickListener(this);
        relativeCache.setOnClickListener(this);

        textLogout = (TextView) findViewById(R.id.textLogout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void initData() {
        if(App.app.isLogin()){
            textLogout.setText(R.string.setting_logout);
            textLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTips();
                }
            });
        }else {
            textLogout.setText(R.string.title_login);
            textLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   goLogin();
                }
            });
        }
        File externalCacheDir = App.app.getExternalCacheDir();
        externalCacheSize = DataCleanManager.getFolderSize(externalCacheDir);
        File cacheDir = App.app.getCacheDir();
        cacheSize = DataCleanManager.getFolderSize(cacheDir);
    }

    private void goLogin() {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogAppCompat);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.setting_logout_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
                LoginUtil.clearUser();
                App.app.setIsLogin(false);
                initData();
            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativeFeedback:
                if(App.app.isLogin()){
                    startActivity(new Intent(this,FeedbackActivity.class));
                }else {
                    goLogin();
                }
                break;
            case R.id.relativeCache:
                showClearCacheTips();
                break;
        }
    }
    private void showClearCacheTips() {
        long totalCacheSize = cacheSize + externalCacheSize;
        if(totalCacheSize <=0){
            ToastUtil.showToast(App.app,"当前没有缓存！");
            return;
        }
        String cacheSizeStr = Formatter.formatFileSize(App.app, totalCacheSize);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogAppCompat);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText("当前缓存为："+cacheSizeStr+"。是否清除？");
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                DataCleanManager.cleanExternalCache(App.app);
                DataCleanManager.cleanInternalCache(App.app);
                initData();

            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }
}
