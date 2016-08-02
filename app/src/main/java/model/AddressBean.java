package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/6/15.
 */
public class AddressBean implements Parcelable {
    public String id;
    public String addr_province = "";
    public String addr_city = "";
    public String addr_county = "";
    public String addr_address;
    public String addr_type;
    public String addr_name;
    public String addr_mobile;



    public AddressBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.addr_province);
        dest.writeString(this.addr_city);
        dest.writeString(this.addr_county);
        dest.writeString(this.addr_address);
        dest.writeString(this.addr_type);
        dest.writeString(this.addr_name);
        dest.writeString(this.addr_mobile);
    }

    protected AddressBean(Parcel in) {
        this.id = in.readString();
        this.addr_province = in.readString();
        this.addr_city = in.readString();
        this.addr_county = in.readString();
        this.addr_address = in.readString();
        this.addr_type = in.readString();
        this.addr_name = in.readString();
        this.addr_mobile = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}
