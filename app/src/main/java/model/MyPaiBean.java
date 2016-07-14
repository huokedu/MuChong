package model;

/**
 * Created by sks on 2016/6/16.
 */
public class MyPaiBean {
    public static final String IS_CREATE_ORDER = "1";
    public String id;//条目id
    public String mybid_commodityid;//商品id
    public String mybid_commodityname;//商品名称
    public String commodity_startprice;//拍卖价
    public String mybid_money;//成交价
    public String mybid_ctime;//创建时间
    public String commodity_coverimg;//商品封面图
    public String paymoney;//需要支付的价格（成交价-保证金）
    public String mybid_iscreateorder;//该竞拍商品是否已经创建订单 "1"创建
}
