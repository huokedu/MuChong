package com.htlc.muchong.util;

import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;

import model.PaiGoodsBean;

/**
 * Created by sks on 2016/6/3.
 */
public class GoodsUtil {
    public static final String IS_TRUE = "1";
    /*根据商品竞拍类型设置类型标签图片*/
    public static void setImageByPaiType(ImageView imageType, String commodity_type) {
        if (PaiGoodsBean.TYPE_DAO.equals(commodity_type)) {
            imageType.setImageResource(R.mipmap.icon_pai_list_dao);
        } else if (PaiGoodsBean.TYPE_WU.equals(commodity_type)) {
            imageType.setImageResource(R.mipmap.icon_pai_list_wu);
        } else if (PaiGoodsBean.TYPE_YOU.equals(commodity_type)) {
            imageType.setImageResource(R.mipmap.icon_pai_list_you);
        }
    }
    /*设置商品价格为：￥100*/
    public static void setBaoYouByFlag(TextView textView, String flag) {
        textView.setText("2".equals(flag)?"包邮":"不包邮");
    }
    /*设置商品价格为：￥100*/
    public static void setPriceBySymbol(TextView textView, String price) {
        textView.setText("￥"+price);
    }
    /*设置商品价格为：￥100*/
    public static void setPriceBySymbolAndOld(TextView textView, String price) {
        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        textView.setText("￥"+price);
    }
    /*设置商品价格为：￥100*/
    public static void setPriceBySymbolAndNew(TextView textView, String price) {
        textView.setText("当前价格:￥"+price);
    }
    /*设置商品价格为：100元*/
    public static void setPriceByYuan(TextView textView, String price) {
        textView.setText(price+"元");
    }
    /*如果flag=1，则为true*/
    public static boolean isTrue(String flag){
        return IS_TRUE.equals(flag);
    }

    /*设置商品数量为 数量： 1*/
    public static void setNumber(TextView textNumber, String num) {
        textNumber.setText("数量:  "+num);
    }
    public static void setDaoJiShiTips(String state, String timeStr, String timeend, TextView textView){
        long time = Long.parseLong(timeStr)*1000;
        long hour = time / 3600000;
        long minute = time % 3600000 / 60000;
        long second = time % 3600000 % 60000 / 1000;
        if (PaiGoodsBean.STATE_NO_START.equals(state)) {
            textView.setText("距离开始:"+hour+"小时"+minute+"分"+second+"秒");
        } else if (PaiGoodsBean.STATE_END.equals(state)) {
            textView.setText("距离结束:0小时0分0秒");
        } else if (PaiGoodsBean.STATE_STARTING.equals(state)) {
            textView.setText("距离结束:"+hour+"小时"+minute+"分"+second+"秒");
        }
    }
}
