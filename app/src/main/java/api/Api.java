package api;

import com.larno.util.okhttp.callback.ResultCallback;

import java.io.File;

/**
 * Api接口
 */
public interface Api {
    String Host = "http://t15.damaimob.com";
    String SmsCode = Host + "/Home/HomeUser/getVerifyCode";
    String Register = Host + "/Home/HomeUser/register";
    String Login = Host + "/Home/HomeUser/login";
    String GetUserInfo = Host + "/Home/HomeUser/getUserinfo";
    String UpdateUserInfo = Host + "/Home/HomeUser/updateUserinfo";
    String ResetPassword = Host + "/Home/HomeUser/resetPwd";

    String Home = Host + "/Home/HomeCommodity/homepage";
    String GetGoodsType = Host + "/Home/HomeCommodity/smallclasslist";

    void smsCode(String mobile, ResultCallback callback);
    void register(String user_account,String Verifycode,String user_pwda,String user_pwdb, ResultCallback callback);
    void login(String user_account,String user_pwd,ResultCallback callback);
    void getUserInfo(ResultCallback callback);
    void updateUserInfo(String userinfo_nickname,String userinfo_address,File userinfo_headportrait, ResultCallback callback);
    void resetPassword(String user_account,String Verifycode,String user_pwda,String user_pwdb, ResultCallback callback);

    void home(ResultCallback callback);
    void getGoodsType(ResultCallback callback);
}
