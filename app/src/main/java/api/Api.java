package api;

import android.util.Pair;

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
    String GetPaiStartTimes = Host + "/Home/HomeCommodity/getbidtimelist";
    String PublishGoods = Host + "/Home/HomeCommodity/publishCommodity";
    String GoodsDetail = Host + "/Home/HomeCommodity/commodityinfo";
    String GoodsCommentList = Host + "/Home/HomeCommodity/evallist";
    String AddGoodsComment = Host + "/Home/HomeCommodity/addeval";
    String QiangTimeList = Host + "/Home/HomeCommodity/fourtime";
    String QiangList = Host + "/Home/HomeCommodity/buylimit";
    String PaiList = Host + "/Home/HomeCommodity/bidlist";
    String JiaoList = Host + "/Home/HomeCommodity/commoditylist";
    String AddLike = Host + "/Home/HomeCommodity/addlike";
    String AddShoppingCart = Host + "/Home/HomeShopCar/addShopCar";

    void smsCode(String mobile, ResultCallback callback);
    void register(String user_account,String Verifycode,String user_pwda,String user_pwdb, ResultCallback callback);
    void login(String user_account,String user_pwd,ResultCallback callback);
    void getUserInfo(ResultCallback callback);
    void updateUserInfo(String userinfo_nickname,String userinfo_address,File userinfo_headportrait, ResultCallback callback);
    void resetPassword(String user_account,String Verifycode,String user_pwda,String user_pwdb, ResultCallback callback);

    void home(ResultCallback callback);
    void getGoodsType(ResultCallback callback);
    void getPointInTimes(ResultCallback callback);
    void publishGoods(String commodity_name,String commodity_content,
                      String commodity_type,String commodity_smallclass,String commodity_spec,String commodity_material,String commodity_panicprice,
                      String commodity_starttime,String commodity_limitend,String commodity_buynum,String commodity_price,String commodity_bond,
                      Pair<String,File>[] images, ResultCallback callback);
    void goodsDetail(String commodity_id,ResultCallback callback);
    void goodsCommentList(String commodity_id,String page,ResultCallback callback);
    void addGoodsComment(String commodityeval_commodityid, String commodityeval_content, ResultCallback callback);
    void qiangTimeList(ResultCallback callback);
    void qiangList(String flag, String page, ResultCallback callback);
    void paiList(String page, ResultCallback callback);
    void jiaoList(String page, ResultCallback callback);
    void addLike(String commodityid,String forumid,String user, ResultCallback callback);
//    void addShoppingCart(String shopcar_commodityid,String shopcar_commoditynum,boolean flag);

}
