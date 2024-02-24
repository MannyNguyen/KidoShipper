package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

public class BeanItem extends BeanBase {
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private int type;

    public BeanItem() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
