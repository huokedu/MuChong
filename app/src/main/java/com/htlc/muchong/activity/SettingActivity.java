package com.htlc.muchong.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LoginUtil;

/**
 * Created by sks on 2016/5/27.
 */
public class SettingActivity extends BaseActivity {
    private TextView textLogout;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_setting);
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
}
