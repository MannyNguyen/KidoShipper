package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

public class BeanStore extends BeanBase {
    @SerializedName("id")
    private int id;
    @SerializedName("shop_name")
    private String shop_name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public BeanStore() {
    }


}
