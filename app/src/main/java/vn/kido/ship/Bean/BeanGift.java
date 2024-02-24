package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeanGift extends BeanBase{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("attribute")
    private List<BeanAttribute> attribute;

    public BeanGift() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public List<BeanAttribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List attribute) {
        this.attribute = attribute;
    }
}

