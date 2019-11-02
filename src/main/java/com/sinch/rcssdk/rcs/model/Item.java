package com.sinch.rcssdk.rcs.model;

public class Item {
    public String title;
    public String description;
    public String price;
    public String imageUrl;
    public String category;
    public String itemId;
    public Item(String title, String description, String price, String imageUrl, String category, String itemId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.itemId = itemId;
    }
}
