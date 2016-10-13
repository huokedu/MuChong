package model;

/**
 * Created by John_Libo on 2016/10/12.
 */
public class ExpressEvent  {
    private String msg;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public ExpressEvent(String msg) {
        super();
        this.msg = msg;
    }
}
