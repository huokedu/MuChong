package core;

import android.util.Pair;

import com.larno.util.okhttp.callback.ResultCallback;

import java.io.File;
import java.util.List;

import model.CangBean;
import model.GoodsCommentBean;
import model.GoodsDetailBean;
import model.GoodsTypeBean;
import model.HomeBean;
import model.JiaoGoodsBean;
import model.PaiGoodsBean;
import model.PointInTimeBean;
import model.PostCommentBean;
import model.PostDetailBean;
import model.QiangListBean;
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
    void updateUserInfo(String userinfo_nickname,String userinfo_address,File userinfo_headportrait, ActionCallbackListener<Void> listener);
    void resetPassword(String user_account,String Verifycode,String user_pwda,String user_pwdb, ActionCallbackListener<Void> listener);

    void home(ActionCallbackListener<HomeBean> listener);
    void getGoodsType(ActionCallbackListener<List<GoodsTypeBean>> listener);
    void getPointInTimes(ActionCallbackListener<List<PointInTimeBean>> listener);
    void publishGoods(String commodity_name, String commodity_content,
                      String commodity_type, String commodity_smallclass, String commodity_spec, String commodity_material, String commodity_panicprice,
                      String commodity_starttime, String commodity_limitend, String commodity_buynum, String commodity_price, String commodity_bond,
                      File coverImageFile,List<File> contentImageFiles,
                      ActionCallbackListener<Void> listener);
    void goodsDetail(String commodity_id,ActionCallbackListener<GoodsDetailBean> listener);
    void goodsCommentList(String commodity_id,int page, ActionCallbackListener<List<GoodsCommentBean>> listener);
    void addGoodsComment(String commodityeval_commodityid, String commodityeval_content, ActionCallbackListener<Void> listener);
    void qiangTimeList(ActionCallbackListener<List<Pair<String,String>>> listener);
    void qiangList(String flag, int page,ActionCallbackListener<QiangListBean> listener);
    void paiList(int page, ActionCallbackListener<List<PaiGoodsBean>> listener);
    void jiaoList(int page, ActionCallbackListener<List<JiaoGoodsBean>> listener);
    void jiaoListBySmallClass(int page, String commodity_smallclass, String order, String commodity_material, ActionCallbackListener<List<JiaoGoodsBean>> listener);
    void addLikeGoods(String commodity_id,ActionCallbackListener<Void> listener);
    void addShoppingCart(String shopcar_commodityid,ActionCallbackListener<Void> listener);

    void cangList(int page,ActionCallbackListener<List<CangBean>> listener);
    void postDetail(String forum_id,ActionCallbackListener<PostDetailBean> listener);
    void postCommentList(String forum_id, int page, ActionCallbackListener<List<PostCommentBean>> listener);
    void addPostComment(String forum_backid, String forum_content, ActionCallbackListener<Void> listener);
}
