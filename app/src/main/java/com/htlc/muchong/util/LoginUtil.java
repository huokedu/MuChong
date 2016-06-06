package com.htlc.muchong.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.LoginActivity;

import model.UserBean;
import model.UserInfoBean;

/**
 * Created by sks on 2016/5/30.
 */
public class LoginUtil {
    private static final String USER_ID = "user_id";
    private static final String USER_ACCOUNT = "user_account";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_ROLE = "user_role";


    private static final String userinfo_nickname = "userinfo_nickname";
    private static final String userinfo_headportrait = "userinfo_headportrait";
    private static final String userinfo_grade = "userinfo_grade";
    private static final String userinfo_money = "userinfo_money";
    private static final String userinfo_likenum = "userinfo_likenum";
    private static final String userinfo_address = "userinfo_address";

    public static UserBean getUser() {
        UserBean bean = new UserBean();
        bean.id = SharedPreferenceUtil.getString(App.app, USER_ID, "");
        bean.user_account = SharedPreferenceUtil.getString(App.app, USER_ACCOUNT, "");
        bean.user_token = SharedPreferenceUtil.getString(App.app, USER_TOKEN, "");
        bean.user_role = SharedPreferenceUtil.getString(App.app, USER_ROLE, "");
        return bean;
    }

    public static void setUser(UserBean bean) {
        if (!TextUtils.isEmpty(bean.id)) {
            SharedPreferenceUtil.putString(App.app, USER_ID, bean.id);
        }
        if (!TextUtils.isEmpty(bean.user_account)) {
            SharedPreferenceUtil.putString(App.app, USER_ACCOUNT, bean.user_account);
        }
        if (!TextUtils.isEmpty(bean.user_token)) {
            SharedPreferenceUtil.putString(App.app, USER_TOKEN, bean.user_token);
        }
        if (!TextUtils.isEmpty(bean.user_role)) {
            SharedPreferenceUtil.putString(App.app, USER_ROLE, bean.user_role);
        }
    }

    public static void clearUser() {
        SharedPreferenceUtil.remove(App.app, USER_ID);
        SharedPreferenceUtil.remove(App.app, USER_ACCOUNT);
        SharedPreferenceUtil.remove(App.app, USER_TOKEN);
        SharedPreferenceUtil.remove(App.app, USER_ROLE);
        clearUserInfo();
    }

    public static UserInfoBean getUserInfo() {
        UserInfoBean bean = new UserInfoBean();
        bean.userinfo_nickname = SharedPreferenceUtil.getString(App.app, userinfo_nickname, "");
        bean.userinfo_headportrait = SharedPreferenceUtil.getString(App.app, userinfo_headportrait, "");
        bean.userinfo_grade = SharedPreferenceUtil.getString(App.app, userinfo_grade, "0");
        bean.userinfo_money = SharedPreferenceUtil.getString(App.app, userinfo_money, "0");
        bean.userinfo_likenum = SharedPreferenceUtil.getString(App.app, userinfo_likenum, "0");
        bean.userinfo_address = SharedPreferenceUtil.getString(App.app, userinfo_address, "");
        return bean;
    }

    public static void setUserInfo(UserInfoBean bean) {
        SharedPreferenceUtil.putString(App.app, userinfo_nickname, bean.userinfo_nickname);
        SharedPreferenceUtil.putString(App.app, userinfo_headportrait, bean.userinfo_headportrait);
        SharedPreferenceUtil.putString(App.app, userinfo_grade, bean.userinfo_grade);
        SharedPreferenceUtil.putString(App.app, userinfo_money, bean.userinfo_money);
        SharedPreferenceUtil.putString(App.app, userinfo_likenum, bean.userinfo_likenum);
        SharedPreferenceUtil.putString(App.app, userinfo_address, bean.userinfo_address);

    }

    public static void clearUserInfo() {
        SharedPreferenceUtil.remove(App.app, userinfo_nickname);
        SharedPreferenceUtil.remove(App.app, userinfo_headportrait);
        SharedPreferenceUtil.remove(App.app, userinfo_grade);
        SharedPreferenceUtil.remove(App.app, userinfo_money);
        SharedPreferenceUtil.remove(App.app, userinfo_likenum);
        SharedPreferenceUtil.remove(App.app, userinfo_address);
    }

    public static void showLoginTips(final Activity activity ){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogAppCompat);
        View view = View.inflate(activity, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.login_no_login_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
                activity.startActivity(new Intent(activity, LoginActivity.class));
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
