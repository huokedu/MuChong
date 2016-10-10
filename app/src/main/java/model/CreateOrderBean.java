package model;

/**
 * Created by John_Libo on 2016/10/10.
 */
public class CreateOrderBean {
    ChargesBean charges;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    private String order_id;
    public ChargesBean getCharges() {
        return charges;
    }

    public void setCharges(ChargesBean charges) {
        this.charges = charges;
    }


}
