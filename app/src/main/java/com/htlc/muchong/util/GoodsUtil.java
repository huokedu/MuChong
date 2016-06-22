package com.htlc.muchong.util;

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
    public static void setPriceBySymbol(TextView textView, String price) {
        textView.setText("￥"+price);
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
}
