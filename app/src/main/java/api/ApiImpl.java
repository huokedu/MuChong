package api;

import android.util.Pair;

import com.htlc.muchong.util.LoginUtil;
import com.larno.util.okhttp.callback.ResultCallback;
import com.larno.util.okhttp.request.OkHttpRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.UserBean;

/**
 * Api实现类
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

    @Override
    public void updateUserInfo(String userinfo_nickname, String userinfo_address, File userinfo_headportrait, ResultCallback callback) {
        UserBean user = LoginUtil.getUser();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user.id);
        params.put("user_token", user.user_token);
        params.put("userinfo_nickname", userinfo_nickname);
        params.put("userinfo_address", userinfo_address);
        String url = Api.UpdateUserInfo;
        if (userinfo_headportrait != null) {
            Pair<String, File> pair = new Pair<>("userinfo_headportrait", userinfo_headportrait);
            new OkHttpRequest.Builder().url(url).params(params).files(pair).upload(callback);
        } else {
            new OkHttpRequest.Builder().url(url).params(params).post(callback);
        }
    }

    @Override
    public void resetPassword(String user_account, String Verifycode, String user_pwda, String user_pwdb, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_account", user_account);
        params.put("Verifycode", Verifycode);
        params.put("user_pwda", user_pwda);
        params.put("user_pwdb", user_pwdb);
        String url = Api.ResetPassword;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void home(ResultCallback callback) {
        String url = Api.Home;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void getGoodsType(ResultCallback callback) {
        String url = Api.GetGoodsType;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void getPointInTimes(ResultCallback callback) {
        String url = Api.GetPaiStartTimes;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void publishGoods(String commodity_name, String commodity_content,
                             String commodity_type, String commodity_smallclass, String commodity_spec, String commodity_material, String commodity_panicprice,
                             String commodity_starttime, String commodity_limitend, String commodity_buynum, String commodity_price, String commodity_bond,
                             Pair<String, File>[] images, ResultCallback callback) {
        UserBean user = LoginUtil.getUser();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user.id);
        params.put("user_token", user.user_token);

        params.put("commodity_name", commodity_name);
        params.put("commodity_content", commodity_content);

        params.put("commodity_type", commodity_type);
        params.put("commodity_smallclass", commodity_smallclass);
        params.put("commodity_spec", commodity_spec);
        params.put("commodity_material", commodity_material);
        params.put("commodity_panicprice", commodity_panicprice);

        params.put("commodity_starttime", commodity_starttime);
        params.put("commodity_timelimit", commodity_limitend);
        params.put("commodity_buynum", commodity_buynum);
        params.put("commodity_price", commodity_price);
        params.put("commodity_bond", commodity_bond);

        String url = Api.PublishGoods;
        new OkHttpRequest.Builder().url(url).params(params).files(images).upload(callback);

    }

    @Override
    public void goodsDetail(String commodity_id, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("commodity_id", commodity_id);
        String url = Api.GoodsDetail;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void goodsCommentList(String commodity_id, ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("commodity_id", commodity_id);
        String url = Api.GoodsCommentList;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }
}
