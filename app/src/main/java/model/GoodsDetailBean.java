package model;

import java.util.List;

/**
 * Created by sks on 2016/6/2.
 */
public class GoodsDetailBean {
    public static final String REN_ZHENG_FLAG = "1";

    public String id;
    public String commodity_name;
    public String commodity_content;
    public String commodity_imgStr;
    public String commodity_likenum;
    public String commodity_looknum;
    public String commodity_panicprice;
    public String commodity_freemail;
    public String commodity_material;
    public String commodity_spec;
    public String userinfo_sincerity;
    public String islike;
    public String evalcount;
    public List<GoodsCommentBean> evallist;


    public String commodity_type;
    public String commodity_limitend;
    public String commodity_limitstart;
    public String commodity_timelimit;
    public String commodity_buynum;
    public String commodity_price;
    public String commodity_startprice;
    public String commodity_bond;
    public String state;
    public String timeStr;
    public String timeend;
    public String bidname;
    public String userinfo_nickname;
    public String userinfo_grade;
    public String userinfo_headportrait;
    public String decpricetime;
    public List<JiaPriceBean> jiajia;


    public String commodity_userid;
    public String commodity_coverimg;
    public String commodity_bigclass;
    public String commodity_smallclass;
    public String constant_name;
    public String commodity_isgrounding;
    public String commodity_ctime;
    public String commodity_utime;

    public class JiaPriceBean{
        public String constant_num;
    }
}
