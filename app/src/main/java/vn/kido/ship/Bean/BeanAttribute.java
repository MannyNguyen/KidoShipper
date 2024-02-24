package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

public class BeanAttribute extends BeanBase{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("value")
    private int value;
    @SerializedName("total_money")
    private int total_money;
    @SerializedName("money")
    private int money;

    public BeanAttribute() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
