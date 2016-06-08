package model;

/**
 * Created by sks on 2016/5/30.
 * 登录用户bean
 */
public class UserBean {
    public static final String TYPE_NORMAL = "1";
    public static final String TYPE_EXPERT = "2";
    public static final String TYPE_MERCHANT = "3";
    public String id;
    public String user_account;
    public String user_token;
    public String user_role;
}
