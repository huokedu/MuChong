package model;

/**
 * Created by John_Libo on 2016/10/10.
 */
public class OrderPayEvent {
    private String msg;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public OrderPayEvent(String msg) {
        super();
        this.msg = msg;
    }
}
