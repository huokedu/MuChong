package core;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CacheUtil;
import com.larno.util.EncryptUtil;
import com.larno.util.NetworkUtil;
import com.larno.util.RegexUtils;
import com.larno.util.okhttp.callback.ResultCallback;

import java.io.File;

import api.Api;
import api.ApiImpl;
import model.UserBean;
import model.UserInfoBean;
import okhttp3.Request;


/**
 * AppAction接口的实现类
 *
 */
public class AppActionImpl implements AppAction {
    public static final String KEY_CODE = "code";
    public static final String KEY_DATA = "data";
    public static final String KEY_MSG = "msg";
    public static final String VALUE_CODE_SUCCESS = "1";
    public static final String VALUE_CODE_FAIL = "0";

    private Context context;
    private Api api;
//    private CacheUtil cacheUtil;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
//        this.cacheUtil = new CacheUtil(context);
    }

    @Override
    public void smsCode(String mobile, final ActionCallbackListener<Void> listener) {
        if(!NetworkUtil.isNetworkAvailable(context)){
            listener.onFailure(ErrorEvent.NETWORK_ERROR,ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if(TextUtils.isEmpty(mobile)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"手机号不能为空");
            return;
        }
        if(!RegexUtils.isMatchPhoneNum(mobile)){
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL,"手机号格式不正确");
            return;
        }
        api.smsCode(mobile, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if(VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))){
                    listener.onSuccess(null);
                }else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void register(String user_account, String Verifycode, String user_pwda, String user_pwdb,boolean isAgreeProtocol, ActionCallbackListener<Void> listener) {
        if(!NetworkUtil.isNetworkAvailable(context)){
            listener.onFailure(ErrorEvent.NETWORK_ERROR,ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if(TextUtils.isEmpty(user_account)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"手机号不能为空");
            return;
        }
        if(!RegexUtils.isMatchPhoneNum(user_account)){
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL,"手机号格式不正确");
            return;
        }
        if(TextUtils.isEmpty(Verifycode)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"验证码不能为空");
            return;
        }
        if(TextUtils.isEmpty(user_pwda)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"密码不能为空");
            return;
        }
        if(!user_pwda.equals(user_pwdb)){
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL,"密码不一致");
            return;
        }
        if(!isAgreeProtocol){
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL,"请阅读并同意相关协议");
            return;
        }
        user_pwda = EncryptUtil.makeMD5(user_pwda);
        user_pwdb = EncryptUtil.makeMD5(user_pwdb);
        api.register(user_account, Verifycode, user_pwda, user_pwdb, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if(VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))){
                    listener.onSuccess(null);
                }else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void login(String user_account, String user_pwd, ActionCallbackListener<UserBean> listener) {
        if(!NetworkUtil.isNetworkAvailable(context)){
            listener.onFailure(ErrorEvent.NETWORK_ERROR,ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if(TextUtils.isEmpty(user_account)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"手机号不能为空");
            return;
        }
        if(TextUtils.isEmpty(user_pwd)){
            listener.onFailure(ErrorEvent.PARAM_NULL,"密码不能为空");
            return;
        }
        user_pwd = EncryptUtil.makeMD5(user_pwd);
        api.login(user_account, user_pwd, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if(VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))){
                    UserBean userBean = JSON.parseObject(model.getString(KEY_DATA), UserBean.class);
                    LoginUtil.setUser(userBean);
                    listener.onSuccess(userBean);
                }else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void getUserInfo(ActionCallbackListener<UserInfoBean> listener) {
        api.getUserInfo(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    UserInfoBean userInfoBean = JSON.parseObject(model.getString(KEY_DATA), UserInfoBean.class);
                    listener.onSuccess(userInfoBean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void updateUserInfo(String userinfo_nickname, String userinfo_address, File userinfo_headportrait, ActionCallbackListener<Void> listener) {
        api.updateUserInfo(userinfo_nickname, userinfo_address, userinfo_headportrait, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if(VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))){
                    listener.onSuccess(null);
                }else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }
}
