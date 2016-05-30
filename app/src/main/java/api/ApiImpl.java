package api;

import com.htlc.muchong.util.LoginUtil;
import com.larno.util.okhttp.callback.ResultCallback;
import com.larno.util.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.Map;

import model.UserBean;

/**
 * Api实现类
 *
 */
public class ApiImpl implements Api {
    @Override
    public void smsCode(String mobile, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        String url = Api.SmsCode;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void register(String user_account, String Verifycode, String user_pwda, String user_pwdb, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_account", user_account);
        params.put("Verifycode", Verifycode);
        params.put("user_pwda", user_pwda);
        params.put("user_pwdb", user_pwdb);
        String url = Api.Register;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void login(String user_account, String user_pwd, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_account", user_account);
        params.put("user_pwd", user_pwd);
        String url = Api.Login;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getUserInfo(ResultCallback callback) {
        UserBean user = LoginUtil.getUser();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user.id);
        params.put("user_token", user.user_token);
        String url = Api.GetUserInfo;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }
}
