package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/6/15.
 */
public class AddressBean implements Parcelable {
    public String id;
    public String addr_address;
    public String addr_type;
    public String addr_name;
    public String addr_mobile;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.addr_address);
        dest.writeString(this.addr_type);
        dest.writeString(this.addr_name);
        dest.writeString(this.addr_mobile);
    }

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        this.id = in.readString();
        this.addr_address = in.readString();
        this.addr_type = in.readString();
        this.addr_name = in.readString();
        this.addr_mobile = in.readString();
    }

    public static final Parcelable.Creator<AddressBean> CREATOR = new Parcelable.Creator<AddressBean>() {
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}
