package core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htlc.muchong.activity.PostPublishActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.EncryptUtil;
import com.larno.util.NetworkUtil;
import com.larno.util.RegexUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import api.Api;
import api.ApiImpl;
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
import model.OrderBean;
import model.OrderDetailBean;
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
    public void ensureSmsCode(String user_account, String Verifycode, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(Verifycode)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            return;
        }
        api.ensureSmsCode(user_account, Verifycode, new DefaultResultCallback(listener) {
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
    public void login(final String user_account, String user_pwd, ActionCallbackListener<UserBean> listener) {
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
                    userBean.user_account = user_account;
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
    public void resetPassword(final String user_account, String Verifycode, String user_pwda, String user_pwdb, ActionCallbackListener<Void> listener) {
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
                    String node = model.getJSONObject(KEY_DATA).getString("node");
                    UserBean bean = new UserBean();
                    bean.user_token = node;
                    bean.user_account = user_account;
                    LoginUtil.setUser(bean);
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void resetTel(final String user_account, ActionCallbackListener<Void> listener) {
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
        api.resetTel(user_account, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    String node = model.getJSONObject(KEY_DATA).getString("node");
                    UserBean bean = new UserBean();
                    bean.user_token = node;
                    bean.user_account = user_account;
                    LoginUtil.setUser(bean);
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void myAddressList(ActionCallbackListener<List<AddressBean>> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.myAddressList(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<AddressBean> bean = JSON.parseArray(model.getString(KEY_DATA), AddressBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void addAddress(String addr_address, String addr_name, String addr_mobile, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(addr_address)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(addr_name)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货人不能为空");
            return;
        }
        if (TextUtils.isEmpty(addr_mobile)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMatchPhoneNum(addr_mobile)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号格式不正确");
            return;
        }
        api.addAddress(addr_address, addr_name, addr_mobile, new DefaultResultCallback(listener) {
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
    public void updateAddress(String addr_id, String addr_address, String addr_name, String addr_mobile, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(addr_address)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(addr_name)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货人不能为空");
            return;
        }
        if (TextUtils.isEmpty(addr_mobile)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMatchPhoneNum(addr_mobile)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号格式不正确");
            return;
        }
        api.updateAddress(addr_id, addr_address, addr_name, addr_mobile, new DefaultResultCallback(listener) {
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
    public void deleteAddress(String addr_id, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.deleteAddress(addr_id, new DefaultResultCallback(listener) {
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
    public void messageList(int page, ActionCallbackListener<List<MessageBean>> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.messageList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<MessageBean> bean = JSON.parseArray(model.getString(KEY_DATA), MessageBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void deleteMessage(String msg_id, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.deleteMessage(msg_id, new DefaultResultCallback(listener) {
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
    public void myPaiList(int page, ActionCallbackListener<List<MyPaiBean>> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.myPaiList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<MyPaiBean> bean = JSON.parseArray(model.getString(KEY_DATA), MyPaiBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void myOrderList(int page, String flag, ActionCallbackListener<List<OrderBean>> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.myOrderList(String.valueOf(page), flag, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<OrderBean> bean = JSON.parseArray(model.getString(KEY_DATA), OrderBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void pay(String order_id, String channel, ActionCallbackListener<CreateOrderResultBean> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.pay(order_id, channel, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    String charges = model.getJSONObject(KEY_DATA).getString("charges");
                    String order_id = model.getJSONObject(KEY_DATA).getString("order_id");
                    CreateOrderResultBean bean = new CreateOrderResultBean();
                    bean.charges = charges;
                    bean.order_id = order_id;
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void myOrderDetail(String order_id, ActionCallbackListener<OrderDetailBean> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.myOrderDetail(order_id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    OrderDetailBean bean = JSON.parseObject(model.getString(KEY_DATA), OrderDetailBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void feedback(String feedback_content, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(feedback_content)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入您的意见");
            return;
        }
        api.feedback(feedback_content, new DefaultResultCallback(listener) {
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
    public void payForAccount(String money, String channel, ActionCallbackListener<CreateOrderResultBean> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(money)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入充值金额");
            return;
        }
        api.payForAccount(money, channel, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    String charges = model.getJSONObject(KEY_DATA).getString("charges");
                    String order_id = model.getJSONObject(KEY_DATA).getString("order_id");
                    CreateOrderResultBean bean = new CreateOrderResultBean();
                    bean.charges = charges;
                    bean.order_id = order_id;
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void home(ActionCallbackListener<HomeBean> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
    public void goodsCommentList(String commodity_id, int page, ActionCallbackListener<List<GoodsCommentBean>> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.goodsCommentList(commodity_id, String.valueOf(page), new DefaultResultCallback(listener) {
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
    public void addGoodsComment(String commodityeval_commodityid, String commodityeval_content, ActionCallbackListener<Void> listener) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(commodityeval_content)) {
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        api.qiangTimeList(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    JSONObject data = JSON.parseObject(model.getString(KEY_DATA));
                    Set<String> keySet = data.keySet();
                    List<Pair<String, String>> list = new ArrayList<>();
                    for (String key : keySet) {
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
        if (TextUtils.isEmpty(commodity_material)) {
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
        if (!NetworkUtil.isNetworkAvailable(context)) {
            listener.onFailure(ErrorEvent.NETWORK_ERROR, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }
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
    public void buyNow(String channel, String commodity_id, String num, String address_id, ActionCallbackListener<CreateOrderResultBean> listener) {
        if (TextUtils.isEmpty(address_id)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择收获地址");
            return;
        }
        api.buyNow(channel, commodity_id, num, address_id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    String charges = model.getJSONObject(KEY_DATA).getString("charges");
                    String order_id = model.getJSONObject(KEY_DATA).getString("order_id");
                    CreateOrderResultBean bean = new CreateOrderResultBean();
                    bean.charges = charges;
                    bean.order_id = order_id;
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void buyByShoppingCart(String channel, List<ShoppingCartItemBean> shoppingCartItemBeans, String address_id, ActionCallbackListener<CreateOrderResultBean> listener) {
        if (TextUtils.isEmpty(address_id)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择收获地址");
            return;
        }
        String shopcar = JSON.toJSONString(shoppingCartItemBeans);
        api.buyByShoppingCart(channel, shopcar, address_id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    String charges = model.getJSONObject(KEY_DATA).getString("charges");
                    String order_id = model.getJSONObject(KEY_DATA).getString("order_id");
                    CreateOrderResultBean bean = new CreateOrderResultBean();
                    bean.charges = charges;
                    bean.order_id = order_id;
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void shoppingCartList(ActionCallbackListener<List<ShoppingCartItemBean>> listener) {
        api.shoppingCartList(new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<ShoppingCartItemBean> bean = JSON.parseArray(model.getString(KEY_DATA), ShoppingCartItemBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void deleteShoppingCart(List<ShoppingCartItemBean> shoppingCartItemBeans, ActionCallbackListener<Void> listener) {
        if (shoppingCartItemBeans.size() <= 0) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择商品");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(ShoppingCartItemBean bean : shoppingCartItemBeans){
            stringBuilder.append(bean.id);
            stringBuilder.append(ProductDetailActivity.SPLIT_FLAG);
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(ProductDetailActivity.SPLIT_FLAG));

        api.deleteShoppingCart(stringBuilder.toString(), new DefaultResultCallback(listener) {
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
    public void addPaiPrice(String commodity_id, String price, ActionCallbackListener<Void> listener) {
        api.addPaiPrice(commodity_id, price, new DefaultResultCallback(listener) {
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
    public void searchGoods(int page, String search, ActionCallbackListener<List<GoodsBean>> listener) {
        api.searchGoods(String.valueOf(page), search, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<GoodsBean> bean = JSON.parseArray(model.getString(KEY_DATA), GoodsBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void cangList(int page, ActionCallbackListener<List<CangBean>> listener) {
        api.cangList(String.valueOf(page), new DefaultResultCallback(listener) {
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
        if (TextUtils.isEmpty(forum_content)) {
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
        api.addLike("", forum_backid, "", new DefaultResultCallback(listener) {
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
    public void publishPostCang(boolean isCangPublish, String forum_title, String forum_content, File coverImageFile, List<File> contentImageFiles, ActionCallbackListener<Void> listener) {
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
        if (isCangPublish) {
            publishType = TYPE_CANG;
        } else {
            publishType = TYPE_JIAN;
        }

        api.publishPost("",forum_title, forum_content, publishType, imageFiles, new DefaultResultCallback(listener) {
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
    public void publishPost(String type, String durationTime, String forum_title, String forum_content, File coverImageFile, List<File> contentImageFiles, ActionCallbackListener<Void> listener) {
        if (type.equals(PostPublishActivity.Publish_Types[2]) && TextUtils.isEmpty(durationTime)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择活动天数");
            return;
        }
        if (!type.equals(PostPublishActivity.Publish_Types[2])) {
           durationTime = "";
        }
        if (!type.equals(PostPublishActivity.Publish_Types[0]) && coverImageFile == null) {
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
        Pair<String, File>[] imageFiles;
        if(!type.equals(PostPublishActivity.Publish_Types[0])){
            imageFiles = new Pair[contentImageFiles.size() + 1];
            imageFiles[0] = new Pair<>("forum_imgs[0]", coverImageFile);
            for (int i = 0; i < contentImageFiles.size(); i++) {
                imageFiles[i + 1] = new Pair<>("forum_imgs[" + (i + 1) + "]", contentImageFiles.get(i));
            }
        }else {
            imageFiles = new Pair[contentImageFiles.size() + 1];
            imageFiles[0] = new Pair<>("forum_imgs[0]", contentImageFiles.get(0));
            for (int i = 0; i < contentImageFiles.size(); i++) {
                imageFiles[i+1] = new Pair<>("forum_imgs[" + (i + 1) + "]", contentImageFiles.get(i));
            }
        }

        api.publishPost(durationTime,forum_title, forum_content, type, imageFiles, new DefaultResultCallback(listener) {
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

    @Override
    public void postList(int page, ActionCallbackListener<List<PostBean>> listener) {
        postListByPersonId(page, "", listener);
    }

    @Override
    public void postListByPersonId(int page, String user_id, ActionCallbackListener<List<PostBean>> listener) {
        api.postList(user_id, String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PostBean> bean = JSON.parseArray(model.getString(KEY_DATA), PostBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void schoolList(int page, ActionCallbackListener<List<SchoolBean>> listener) {
        schoolListByPersonId(page, "", listener);
    }

    @Override
    public void schoolListByPersonId(int page, String user_id, ActionCallbackListener<List<SchoolBean>> listener) {
        api.schoolList(user_id, String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<SchoolBean> bean = JSON.parseArray(model.getString(KEY_DATA), SchoolBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void activityList(int page, ActionCallbackListener<List<ActivityBean>> listener) {
        api.activityList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<ActivityBean> bean = JSON.parseArray(model.getString(KEY_DATA), ActivityBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void personList(int page, ActionCallbackListener<List<PersonBean>> listener) {
        api.personList(String.valueOf(page), new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PersonBean> bean = JSON.parseArray(model.getString(KEY_DATA), PersonBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void cangListByPersonId(int page, String user_id, ActionCallbackListener<List<JianBean>> listener) {
        api.cangListByPersonId(String.valueOf(page), user_id, new DefaultResultCallback(listener) {
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
    public void addLikePerson(String user, ActionCallbackListener<Void> listener) {
        api.addLike("", "", user, new DefaultResultCallback(listener) {
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
    public void getPersonInfo(String id, ActionCallbackListener<PersonInfoBean> listener) {
        api.getPersonInfo(id, new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    PersonInfoBean bean = JSON.parseObject(model.getString(KEY_DATA), PersonInfoBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void likeListByTypeOfProduct(int page, String id, ActionCallbackListener<List<GoodsBean>> listener) {
        api.likeListByType(String.valueOf(page), id, "1", new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<GoodsBean> bean = JSON.parseArray(model.getString(KEY_DATA), GoodsBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void likeListByTypeOfCang(int page, String id, ActionCallbackListener<List<JianBean>> listener) {
        api.likeListByType(String.valueOf(page), id, "2", new DefaultResultCallback(listener) {
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
    public void likeListByTypeOfSchool(int page, String id, ActionCallbackListener<List<SchoolBean>> listener) {
        api.likeListByType(String.valueOf(page), id, "3", new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<SchoolBean> bean = JSON.parseArray(model.getString(KEY_DATA), SchoolBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void likeListByTypeOfJian(int page, String id, ActionCallbackListener<List<JianBean>> listener) {
        api.likeListByType(String.valueOf(page), id, "4", new DefaultResultCallback(listener) {
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
    public void likeListByTypeOfPerson(int page, String id, ActionCallbackListener<List<PersonBean>> listener) {
        api.likeListByType(String.valueOf(page), id, "5", new DefaultResultCallback(listener) {
            @Override
            public void onResponse(String response) {
                JSONObject model = JSON.parseObject(response);
                if (VALUE_CODE_SUCCESS.equals(model.getString(KEY_CODE))) {
                    List<PersonBean> bean = JSON.parseArray(model.getString(KEY_DATA), PersonBean.class);
                    listener.onSuccess(bean);
                } else {
                    listener.onFailure(ErrorEvent.SEVER_ILLEGAL, model.getString(KEY_MSG));
                }
            }
        });
    }

    @Override
    public void myJianList(int page, String forum_yesorno, ActionCallbackListener<List<JianBean>> listener) {
        api.myJianList(String.valueOf(page), forum_yesorno, new DefaultResultCallback(listener) {
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

}
