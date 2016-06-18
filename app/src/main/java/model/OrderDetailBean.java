package model;

import java.util.List;

/**
 * Created by sks on 2016/6/18.
 */
public class OrderDetailBean {
    public String order_no;
    public String order_money;
    public Address order_address;
    public String order_payment;
    public List<ShoppingCartItemBean> list;
    public class Address{
        public String name;
        public String mobile;
        public String address;
    }


}
