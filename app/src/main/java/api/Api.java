package api;

import com.larno.util.okhttp.callback.ResultCallback;

/**
 * Api接口
 */
public interface Api {
    String Host = "http://t15.damaimob.com";
    String SmsCode = Host + "/Home/HomeUser/getVerifyCode";
    String Register = Host + "/Home/HomeUser/register";
    String Login = Host + "/Home/HomeUser/login";
    String GetUserInfo = Host + "/Home/HomeUser/getUserinfo";

    void smsCode(String mobile, ResultCallback callback);
    void register(String user_account,String Verifycode,String user_pwda,String user_pwdb, ResultCallback callback);
    void login(String user_account,String user_pwd,ResultCallback callback);
    void getUserInfo(ResultCallback callback);
}
