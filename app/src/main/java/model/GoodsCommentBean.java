package model;

/**
 * Created by sks on 2016/6/2.
 */
public class GoodsCommentBean {
    public static final String HTML =   "<font color=\"#CD8E58\">回复:%1$s  </font>" + "<font color=\"#808080\">%2$s</font>";
    public static final String IS_REPLAY = "1";
    public String id;
    public String commodityeval_userid;
    public String commodityeval_content;
    public String userinfo_nickname;
    public String userinfo_grade;
    public String userinfo_headportrait;
    public String isreplay;//是否是回复 1是
    public String replayuser;//被回复人的昵称

}
