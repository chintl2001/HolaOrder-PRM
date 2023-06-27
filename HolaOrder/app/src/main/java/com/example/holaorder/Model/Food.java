package com.example.holaorder.Model;

public class Food {
    private String id;
    private String name;
    private String image;
    private String categoryId;
    private String description;
    private String discount;
    private String price;
    private float rate;

    public Food() {
    }

    public Food(String id, String name, String image, String categoryId, String description, String discount, String price, float rate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.categoryId = categoryId;
        this.description = description;
        this.discount = discount;
        this.price = price;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
