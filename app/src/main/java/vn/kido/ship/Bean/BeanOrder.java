package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

public class BeanOrder extends BeanBase {
    @SerializedName("id")
    private int id;
    @SerializedName("order_code")
    private String order_code;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("total_money")
    private int total_money;
    @SerializedName("suggest_time")
    private long suggest_time;
    @SerializedName("create_date")
    private long create_date;
    @SerializedName("type")
    private int type;
    @SerializedName("delivered_date")
    private long delivered_date;
    @SerializedName("note")
    private String note;
    @SerializedName("reason")
    private String reason;
    @SerializedName("address_delivery")
    private String address_delivery;
    @SerializedName("shop_name")
    private String shop_name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("order_step")
    private int order_step;
    @SerializedName("order_type_name")
    private String order_type_name;

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public BeanOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public long getSuggest_time() {
        return suggest_time;
    }

    public void setSuggest_time(long suggest_time) {
        this.suggest_time = suggest_time;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(long delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddress_delivery() {
        return address_delivery;
    }

    public void setAddress_delivery(String address_delivery) {
        this.address_delivery = address_delivery;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getOrder_step() {
        return order_step;
    }

    public void setOrder_step(int order_step) {
        this.order_step = order_step;
    }

    public String getOrder_type_name() {
        return order_type_name;
    }

    public void setOrder_type_name(String order_type_name) {
        this.order_type_name = order_type_name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
