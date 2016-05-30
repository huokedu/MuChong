package core;

import com.larno.util.okhttp.callback.ResultCallback;

import model.UserBean;
import model.UserInfoBean;

/**
 * 接收app层的各种Action
 *
 */
public interface AppAction {
    void smsCode(String mobile, ActionCallbackListener<Void> listener);
    void register(String user_account,String Verifycode,String user_pwda,String user_pwdb, boolean isAgreeProtocol,ActionCallbackListener<Void> listener);
    void login(String user_account,String user_pwd, ActionCallbackListener<UserBean> listener);
    void getUserInfo(ActionCallbackListener<UserInfoBean> listener);
}
