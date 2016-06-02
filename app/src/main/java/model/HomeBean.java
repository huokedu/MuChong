package model;

import java.util.List;

/**
 * Created by sks on 2016/6/2.
 */
public class HomeBean {
    public List<BannerBean> banner;
    public List<GoodsTypeBean> smallclass;
    public QiangListBean limittime;
    public List<PaiGoodsBean> bid;
    public List<GoodsBean> jingpin;
    public class QiangListBean{
        public String state;
        public String timeStr;
        public String timeend;
        public List<GoodsBean> list;
    }
}
