package com.htlc.muchong.util;

import android.text.TextUtils;

import com.htlc.muchong.App;

import model.UserBean;

/**
 * Created by sks on 2016/5/30.
 */
public class LoginUtil {
    private static final String USER_ID = "user_id";
    private static final String USER_ACCOUNT = "user_account";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_ROLE = "user_role";
    public static UserBean getUser() {
        UserBean bean = new UserBean();
        bean.id = SharedPreferenceUtil.getString(App.app,USER_ID,"");
        bean.user_account = SharedPreferenceUtil.getString(App.app,USER_ACCOUNT,"");
        bean.user_token = SharedPreferenceUtil.getString(App.app,USER_TOKEN,"");
        bean.user_role = SharedPreferenceUtil.getString(App.app,USER_ROLE,"");
        return bean;
    }
    public static void setUser(UserBean bean){
        if(!TextUtils.isEmpty(bean.id)){
            SharedPreferenceUtil.putString(App.app,USER_ID,bean.id);
        }
        if(!TextUtils.isEmpty(bean.id)){
            SharedPreferenceUtil.putString(App.app,USER_ACCOUNT,bean.user_account);
        }
        if(!TextUtils.isEmpty(bean.id)){
            SharedPreferenceUtil.putString(App.app,USER_TOKEN,bean.user_token);
        }
        if(!TextUtils.isEmpty(bean.id)){
            SharedPreferenceUtil.putString(App.app,USER_ROLE,bean.user_role);
        }
    }
    public static void clearUser(){
        SharedPreferenceUtil.remove(App.app,USER_ID);
        SharedPreferenceUtil.remove(App.app,USER_ACCOUNT);
        SharedPreferenceUtil.remove(App.app,USER_TOKEN);
        SharedPreferenceUtil.remove(App.app,USER_ROLE);
    }
}
