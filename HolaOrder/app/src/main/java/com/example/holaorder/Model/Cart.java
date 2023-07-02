package com.example.holaorder.Model;

public class Cart {
    private String fname;
    private String price;
    private String quantity;
    private String image;

    public Cart() {
    }

    public Cart(String fname, String price, String quantity, String image) {
        this.fname = fname;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


