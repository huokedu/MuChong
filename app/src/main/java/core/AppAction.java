package core;

import android.util.Pair;

import java.io.File;
import java.util.List;

import model.ActivityBean;
import model.AddressBean;
import model.CangBean;
import model.CreateOrderResultBean;
import model.GoodsBean;
import model.GoodsCommentBean;
import model.GoodsDetailBean;
import model.GoodsTypeBean;
import model.HomeBean;
import model.JianBean;
import model.JiaoGoodsBean;
import model.MessageBean;
import model.MyPaiBean;
import model.PaiGoodsBean;
import model.PersonBean;
import model.PersonInfoBean;
import model.PointInTimeBean;
import model.PostBean;
import model.PostCommentBean;
import model.PostDetailBean;
import model.QiangListBean;
import model.SchoolBean;
import model.ShoppingCartItemBean;
import model.UserBean;
import model.UserInfoBean;

/**
 * 接收app层的各种Action
 *
 */
public interface AppAction {
    void smsCode(String mobile, ActionCallbackListener<Void> listener);
    void ensureSmsCode(String user_account, String Verifycode, ActionCallbackListener<Void> listener);
    void register(String user_account,String Verifycode,String user_pwda,String user_pwdb, boolean isAgreeProtocol,ActionCallbackListener<Void> listener);
    void login(String user_account,String user_pwd, ActionCallbackListener<UserBean> listener);
    void getUserInfo(ActionCallbackListener<UserInfoBean> listener);
    void updateUserInfo(String userinfo_nickname,String userinfo_address,File userinfo_headportrait, ActionCallbackListener<Void> listener);
    void resetPassword(String user_account,String Verifycode,String user_pwda,String user_pwdb, ActionCallbackListener<Void> listener);
    void resetTel(String user_account, ActionCallbackListener<Void> listener);
    void myAddressList(ActionCallbackListener<List<AddressBean>> listener);
    void addAddress(String addr_address, String addr_name, String addr_mobile, ActionCallbackListener<Void> listener);
    void updateAddress(String addr_id, String addr_address, String addr_name, String addr_mobile, ActionCallbackListener<Void> listener);
    void deleteAddress(String addr_id, ActionCallbackListener<Void> listener);
    void messageList(int page,  ActionCallbackListener<List<MessageBean>> listener);
    void myPaiList(int page,  ActionCallbackListener<List<MyPaiBean>> listener);

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
    void buyNow(String commodity_id, String num, String address_id, ActionCallbackListener<CreateOrderResultBean> listener);
    void buyByShoppingCart(List<ShoppingCartItemBean> shoppingCartItemBeans, String address_id, ActionCallbackListener<CreateOrderResultBean> listener);
    void shoppingCartList(ActionCallbackListener<List<ShoppingCartItemBean>> listener);
    void deleteShoppingCart(List<ShoppingCartItemBean> shoppingCartItemBeans, ActionCallbackListener<Void> listener);
    void addPaiPrice(String commodity_id, String price, ActionCallbackListener<Void> listener);

    void cangList(int page,ActionCallbackListener<List<CangBean>> listener);
    void postDetail(String forum_id,ActionCallbackListener<PostDetailBean> listener);
    void postCommentList(String forum_id, int page, ActionCallbackListener<List<PostCommentBean>> listener);
    void addPostComment(String forum_backid, String forum_content, ActionCallbackListener<Void> listener);
    void addLikePost(String forum_backid, ActionCallbackListener<Void> listener);
    void publishPostCang(boolean isCangPublish,String forum_title, String forum_content, File coverImageFile, List<File> contentImageFiles,  ActionCallbackListener<Void> listener);
    void jianList(int page, String forum_yesorno,  ActionCallbackListener<List<JianBean>> listener);
    void publishJianResult(String appraisal_forumid, boolean isTrue, String appraisal_content,  ActionCallbackListener<Void> listener);
    void postList(int page, ActionCallbackListener<List<PostBean>> listener);
    void postListByPersonId(int page, String user_id, ActionCallbackListener<List<PostBean>> listener);
    void schoolList(int page, ActionCallbackListener<List<SchoolBean>> listener);
    void schoolListByPersonId(int page, String user_id, ActionCallbackListener<List<SchoolBean>> listener);
    void activityList(int page, ActionCallbackListener<List<ActivityBean>> listener);
    void personList(int page, ActionCallbackListener<List<PersonBean>> listener);
    void cangListByPersonId(int page, String user_id,  ActionCallbackListener<List<JianBean>> listener);
    void addLikePerson(String user, ActionCallbackListener<Void> listener);
    void getPersonInfo(String id, ActionCallbackListener<PersonInfoBean> listener);
    void likeListByTypeOfProduct(int page, String id, ActionCallbackListener<List<GoodsBean>> listener);
    void likeListByTypeOfCang(int page, String id, ActionCallbackListener<List<JianBean>> listener);
    void likeListByTypeOfSchool(int page, String id, ActionCallbackListener<List<SchoolBean>> listener);
    void likeListByTypeOfJian(int page, String id, ActionCallbackListener<List<JianBean>> listener);
    void likeListByTypeOfPerson(int page, String id, ActionCallbackListener<List<PersonBean>> listener);
    void myJianList(int page, String forum_yesorno,  ActionCallbackListener<List<JianBean>> listener);
}
