package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/5/27.
 * 购物车bean
 */
public class ShoppingCartItemBean implements Parcelable {
    public int maxNumber = 10;

    public String id;
    public String shopcar_commodityid;
    public String commodity_name;
    public String commodity_coverimg;
    public String commodity_panicprice;
    public String num;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxNumber);
        dest.writeString(this.id);
        dest.writeString(this.shopcar_commodityid);
        dest.writeString(this.commodity_name);
        dest.writeString(this.commodity_coverimg);
        dest.writeString(this.commodity_panicprice);
        dest.writeString(this.num);
    }

    public ShoppingCartItemBean() {
    }

    protected ShoppingCartItemBean(Parcel in) {
        this.maxNumber = in.readInt();
        this.id = in.readString();
        this.shopcar_commodityid = in.readString();
        this.commodity_name = in.readString();
        this.commodity_coverimg = in.readString();
        this.commodity_panicprice = in.readString();
        this.num = in.readString();
    }

    public static final Parcelable.Creator<ShoppingCartItemBean> CREATOR = new Parcelable.Creator<ShoppingCartItemBean>() {
        public ShoppingCartItemBean createFromParcel(Parcel source) {
            return new ShoppingCartItemBean(source);
        }

        public ShoppingCartItemBean[] newArray(int size) {
            return new ShoppingCartItemBean[size];
        }
    };
}
