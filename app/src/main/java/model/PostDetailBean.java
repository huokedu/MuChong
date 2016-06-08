package model;

import java.util.List;

/**
 * Created by sks on 2016/6/7.
 */
public class PostDetailBean {
    public String id;
    public String forum_userid;
    public String userinfo_nickname;
    public String userinfo_grade;
    public String userinfo_headportrait;

    public String forum_title;
    public String forum_content;
    public String forum_coverimg;
    public String forum_imgstr;
    public String forum_likenum;
    public String forum_looknum;
    public String forum_type;
    public String forum_ctime;//创建时间

    public String evalcount;
    public List<PostCommentBean> evallist;


    /*鉴定真假*/
    public String forum_yesorno;
    public List<JianResultBean> appraisal;



    /*活动*/
    public String forum_starttime;
    public String forum_endtime;


}
