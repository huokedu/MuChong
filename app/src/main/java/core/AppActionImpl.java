package core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CacheUtil;
import com.larno.util.EncryptUtil;
import com.larno.util.NetworkUtil;
import com.larno.util.RegexUtils;
import com.larno.util.okhttp.callback.ResultCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import api.Api;
import api.ApiImpl;
import model.CangBean;
import model.GoodsCommentBean;
import model.GoodsDetailBean;
import model.GoodsTypeBean;
import model.HomeBean;
import model.JianBean;
import model.JiaoGoodsBean;
import model.PaiGoodsBean;
import model.PointInTimeBean;
import model.PostCommentBean;
import model.PostDetailBean;
import model.QiangListBean;
import model.UserBean;
import model.UserInfoBean;
import okhttp3.Request;


/**
 * AppAction接口的实现类
 */
public class AppActionImpl implements AppAction {
    public static final String KEY_CODE = "code";
    public static final String KEY_DATA = "data";
    public static final String KEY_MSG = "msg";
    public static final String VALUE_CODE_SUCCESS = "1";
    public static final String VALUE_CODE_FAIL = "0";
    public static final int PAGE_SIZE = 10;

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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMatchPhoneNum(mobile)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号格式不正确");
            return;
        }
        api.smsCode(mobile, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void register(String user_account, String Verifycode, String user_pwda, String user_pwdb, boolean isAgreeProtocol, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(user_account)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMatchPhoneNum(user_account)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(Verifycode)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_pwda)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (!user_pwda.equals(user_pwdb)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "密码不一致");
            return;
        }
        if (!isAgreeProtocol) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "请阅读并同意相关协议");
            return;
        }
        user_pwda = EncryptUtil.makeMD5(user_pwda);
        user_pwdb = EncryptUtil.makeMD5(user_pwdb);
        api.register(user_account, Verifycode, user_pwda, user_pwdb, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void login(String user_account, String user_pwd, ActionCallbackListener<UserBean> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(user_account)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        user_pwd = EncryptUtil.makeMD5(user_pwd);
        api.login(user_account, user_pwd, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    UserBean userBean = JSON.parseObject(model.getString(KEY_DATA), UserBean.class);
                    LoginUtil.setUser(userBean);
                    listener.onSuccess(userBean);
                } else {
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
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void resetPassword(String user_account, String Verifycode, String user_pwda, String user_pwdb, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(user_account)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMatchPhoneNum(user_account)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(Verifycode)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_pwda)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (!user_pwda.equals(user_pwdb)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "密码不一致");
            return;
        }
        user_pwda = EncryptUtil.makeMD5(user_pwda);
        user_pwdb = EncryptUtil.makeMD5(user_pwdb);
        api.resetPassword(user_account, Verifycode, user_pwda, user_pwdb, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void home(ActionCallbackListener<HomeBean> listener) {
        api.home(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    HomeBean bean = JSON.parseObject(model.getString(KEY_DATA), HomeBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void getGoodsType(ActionCallbackListener<List<GoodsTypeBean>> listener) {
        api.getGoodsType(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<GoodsTypeBean> bean = JSON.parseArray(model.getString(KEY_DATA), GoodsTypeBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void getPointInTimes(ActionCallbackListener<List<PointInTimeBean>> listener) {
        api.getPointInTimes(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PointInTimeBean> bean = JSON.parseArray(model.getString(KEY_DATA), PointInTimeBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void publishGoods(String commodity_name, String commodity_content, String commodity_type, String commodity_smallclass, String commodity_spec, String commodity_material, String commodity_panicprice,
                             String commodity_starttime, String commodity_limitend, String commodity_buynum, String commodity_price, String commodity_bond,
                             File coverImageFile, List<File> contentImageFiles, ActionCallbackListener<Void> listener) {
        if (coverImageFile == null) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "封面图片不能为空");
            return;
        }
        if (contentImageFiles == null || contentImageFiles.size() == 0) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "内容图片不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_name)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "标题(商品名称)不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_content)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)简介不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_type)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_smallclass)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)分类不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_spec)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)规格不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_material)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)材质不能为空");
            return;
        }
        if (TextUtils.isEmpty(commodity_panicprice)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)价格不能为空");
            return;
        }
        if ("1".equals(commodity_type)) {
            commodity_starttime = "";
            commodity_limitend = "";
            commodity_buynum = "";
            commodity_price = "";
            commodity_bond = "";
        } else if ("2".equals(commodity_type)) {
            if (TextUtils.isEmpty(commodity_starttime)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "开始时间不能为空");
                return;
            }
            if (TextUtils.isEmpty(commodity_buynum)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)总数量不能为空");
                return;
            }
            if (TextUtils.isEmpty(commodity_price)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)市场价格不能为空");
                return;
            }
            commodity_limitend = "";
            commodity_bond = "";
        } else if ("3".equals(commodity_type) || "4".equals(commodity_type) || "5".equals(commodity_type)) {
            if (TextUtils.isEmpty(commodity_starttime)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "开始时间不能为空");
                return;
            }
            if (TextUtils.isEmpty(commodity_limitend)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "持续时间不能为空");
                return;
            }
            if (TextUtils.isEmpty(commodity_price)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "(商品)市场价格不能为空");
                return;
            }
            if (TextUtils.isEmpty(commodity_bond)) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "保证金不能为空");
                return;
            }
            commodity_buynum = "";
        } else {
            return;
        }
        Pair<String, File>[] imageFiles = new Pair[contentImageFiles.size() + 1];
        imageFiles[0] = new Pair<>("commodity_imgs[0]", coverImageFile);
        for (int i = 0; i < contentImageFiles.size(); i++) {
            imageFiles[i + 1] = new Pair<>("commodity_imgs[" + (i + 1) + "]", contentImageFiles.get(i));
        }
        api.publishGoods(commodity_name, commodity_content, commodity_type, commodity_smallclass, commodity_spec, commodity_material, commodity_panicprice,
                commodity_starttime, commodity_limitend, commodity_buynum, commodity_price, commodity_bond, imageFiles, new DefaultResultCallback(listener) {
                    @Override
                    public void onResponse(String response) {
                        JSONObject model = JSON.parseObject(response);
                        if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                            listener.onSuccess(null);
                        } else {
                            listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                        }
                    }
                });

    }

    @Override
    public void goodsDetail(String commodity_id, ActionCallbackListener<GoodsDetailBean> listener) {
        api.goodsDetail(commodity_id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    GoodsDetailBean bean = JSON.parseObject(model.getString(KEY_DATA), GoodsDetailBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void goodsCommentList(String commodity_id,int page, ActionCallbackListener<List<GoodsCommentBean>> listener) {
        api.goodsCommentList(commodity_id,String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<GoodsCommentBean> bean = JSON.parseArray(model.getString(KEY_DATA), GoodsCommentBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addGoodsComment(String commodityeval_commodityid, String commodityeval_content,ActionCallbackListener<Void> listener) {
        if(TextUtils.isEmpty(commodityeval_content)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入评论内容");
            return;
        }
        api.addGoodsComment(commodityeval_commodityid, commodityeval_content, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void qiangTimeList(ActionCallbackListener<List<Pair<String, String>>> listener) {
        api.qiangTimeList(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    JSONObject data = JSON.parseObject(model.getString(KEY_DATA));
                    Set<String> keySet = data.keySet();
                    List<Pair<String, String>> list = new ArrayList<>();
                    for(String key : keySet){
                        String value = data.getString(key);
                        Pair<String, String> pair = new Pair<>(key, value);
                        list.add(pair);
                    }
                    Collections.sort(list, new Comparator<Pair<String, String>>() {
                        @Override
                        public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
                            return lhs.first.compareTo(rhs.first);
                        }
                    });
                    listener.onSuccess(list);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void qiangList(String flag, int page, ActionCallbackListener<QiangListBean> listener) {
            api.qiangList(flag, String.valueOf(page), new DefaultResultCallback(listener) {
                @Override
                public void onResponse(String response) {
                    JSONObject model = JSON.parseObject(response);
                    if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                        QiangListBean bean = JSON.parseObject(model.getString(KEY_DATA), QiangListBean.class);
                        listener.onSuccess(bean);
                    } else {
                        listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                    }
                }
            });
    }

    @Override
    public void paiList(int page, ActionCallbackListener<List<PaiGoodsBean>> listener) {
        api.paiList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PaiGoodsBean> bean = JSON.parseArray(model.getString(KEY_DATA), PaiGoodsBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void jiaoList(int page, ActionCallbackListener<List<JiaoGoodsBean>> listener) {
        api.jiaoList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<JiaoGoodsBean> bean = JSON.parseArray(model.getString(KEY_DATA), JiaoGoodsBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void jiaoListBySmallClass(int page, String commodity_smallclass, String order, String commodity_material, ActionCallbackListener<List<JiaoGoodsBean>> listener) {
        if(TextUtils.isEmpty(commodity_material)){
            commodity_material = "";
        }
        api.jiaoListBySmallClass(String.valueOf(page), commodity_smallclass, order, commodity_material, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<JiaoGoodsBean> bean = JSON.parseArray(model.getString(KEY_DATA), JiaoGoodsBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addLikeGoods(String commodity_id, ActionCallbackListener<Void> listener) {
        api.addLike(commodity_id, "", "", new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addShoppingCart(String shopcar_commodityid, ActionCallbackListener<Void> listener) {
        api.addShoppingCart(shopcar_commodityid, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void cangList(int page,ActionCallbackListener<List<CangBean>> listener) {
        api.cangList(String.valueOf(page),new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<CangBean> bean = JSON.parseArray(model.getString(KEY_DATA), CangBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void postDetail(String forum_id, ActionCallbackListener<PostDetailBean> listener) {
        api.postDetail(forum_id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    PostDetailBean bean = JSON.parseObject(model.getString(KEY_DATA), PostDetailBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void postCommentList(String forum_id, int page, ActionCallbackListener<List<PostCommentBean>> listener) {
        api.postCommentList(forum_id, String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PostCommentBean> bean = JSON.parseArray(model.getString(KEY_DATA), PostCommentBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addPostComment(String forum_backid, String forum_content, ActionCallbackListener<Void> listener) {
        if(TextUtils.isEmpty(forum_content)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入评论内容");
            return;
        }
        api.addPostComment(forum_backid, forum_content, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addLikePost(String forum_backid, ActionCallbackListener<Void> listener) {
        api.addLike("",forum_backid,"", new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void publishPostCang(boolean isCangPublish,String forum_title, String forum_content, File coverImageFile, List<File> contentImageFiles, ActionCallbackListener<Void> listener) {
        if (coverImageFile == null) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "封面图片不能为空");
            return;
        }
        if (contentImageFiles == null || contentImageFiles.size() == 0) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "内容图片不能为空");
            return;
        }
        if (TextUtils.isEmpty(forum_title)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(forum_content)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "文本内容不能为空");
            return;
        }
        Pair<String, File>[] imageFiles = new Pair[contentImageFiles.size() + 1];
        imageFiles[0] = new Pair<>("forum_imgs[0]", coverImageFile);
        for (int i = 0; i < contentImageFiles.size(); i++) {
            imageFiles[i + 1] = new Pair<>("forum_imgs[" + (i + 1) + "]", contentImageFiles.get(i));
        }
        final String TYPE_CANG = "1";
        final String TYPE_JIAN = "6";
        String publishType;
        if(isCangPublish){
            publishType = TYPE_CANG;
        }else {
            publishType = TYPE_JIAN;
        }

        api.publishPost(forum_title, forum_content, publishType, imageFiles, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void jianList(int page, String forum_yesorno, ActionCallbackListener<List<JianBean>> listener) {
        api.jianList(String.valueOf(page), forum_yesorno, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<JianBean> bean = JSON.parseArray(model.getString(KEY_DATA), JianBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void publishJianResult(String appraisal_forumid, boolean isTrue, String appraisal_content, ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(appraisal_content)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "评论内容不能为空");
            return;
        }
        String appraisal_type = isTrue ? "1" : "2";
        api.publishJianResult(appraisal_forumid, appraisal_type, appraisal_content, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }
}
