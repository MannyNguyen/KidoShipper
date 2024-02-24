package vn.kido.ship.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class BeanProduct extends BeanBase {
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_image")
    private String product_image;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private int price;
    @SerializedName("id")
    private int id;
    @SerializedName("order_code")
    private String order_code;
    @SerializedName("type")
    private int type;
    @SerializedName("total_money")
    private int total_money;
    @SerializedName("min_unit")
    private String min_unit;
    @SerializedName("reason")
    private String reason;
    @SerializedName("max_unit")
    private String max_unit;
    @SerializedName("price_max_unit")
    private String price_max_unit;
    @SerializedName("total_pay")
    private int total_pay;
    @SerializedName("total_percent_discount ")
    private double total_percent_discount ;
    @SerializedName("attribute")
    private List<BeanAttribute> attribute;

    private List<BeanAttribute> originAttribute;

    private int origin_total_money;

    @SerializedName("attribute_received")
    private List<BeanAttribute> attribute_received;
    @SerializedName("total_product_price")
    private int total_product_price;
    @SerializedName("total_price")
    private int total_price;

    @Expose(serialize = false)
    public List<BeanAttribute> editableAttribute = null;


    public BeanProduct() {
        
    }

    public BeanProduct(boolean copyAtttributes) {
    }

    public BeanProduct(BeanProduct product) {
        product_name = product.product_name;
        product_image = product.product_image;
        quantity = product.quantity;
        price = product.price;
        id = product.id;
        order_code = product.order_code;
        type = product.type;
        total_money = product.total_money;
        min_unit = product.min_unit;
        max_unit = product.max_unit;
        attribute = product.attribute;
        attribute_received = product.attribute_received;
        total_product_price = product.total_product_price;
        total_price = product.total_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMin_unit() {
        return min_unit;
    }

    public void setMin_unit(String min_unit) {
        this.min_unit = min_unit;
    }

    public String getMax_unit() {
        return max_unit;
    }

    public void setMax_unit(String max_unit) {
        this.max_unit = max_unit;
    }

    public String getPrice_max_unit() {
        return price_max_unit;
    }

    public void setPrice_max_unit(String price_max_unit) {
        this.price_max_unit = price_max_unit;
    }

    public int getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(int total_pay) {
        this.total_pay = total_pay;
    }

    public double getTotal_percent_discount() {
        return total_percent_discount;
    }

    public void setTotal_percent_discount(double total_percent_discount) {
        this.total_percent_discount = total_percent_discount;
    }

    public List<BeanAttribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List attribute) {
        this.attribute = attribute;
    }

    public List<BeanAttribute> getAttribute_received() {
        return attribute_received;
    }

    public void setAttribute_received(List attribute_received) {
        this.attribute_received = attribute_received;
    }

    public int getTotal_product_price() {
        return total_product_price;
    }

    public void setTotal_product_price(int total_product_price) {
        this.total_product_price = total_product_price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<BeanAttribute> getOriginAttribute() {
        return originAttribute;
    }

    public void setOriginAttribute(List<BeanAttribute> originAttribute) {
        this.originAttribute =  new ArrayList<BeanAttribute>(originAttribute);
    }

    public int getOrigin_total_money(){
        return origin_total_money;
    }
    public void setOrigin_total_money(int origin_total_money){
        this.origin_total_money= origin_total_money;
    }
}
