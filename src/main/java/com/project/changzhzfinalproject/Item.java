package com.project.changzhzfinalproject;

public class Item {

    private String id;
    private String category;
    private String color;
    private String colorCode;
    private String size;
    private double price;
    private double discountPrice;
    private String description;
    private int amount;

    public Item(String id, String category, String color, String colorCode, String size, double price, String description) {
        this.id = id;
        this.category = category;
        this.color = color;
        this.colorCode = colorCode;
        this.size = size;
        this.price = price;
        this.description = description;
    }

    public Item(String id, String category, String color, String colorCode, String size, double price,double discountPrice, String description) {
        this.id = id;
        this.category = category;
        this.color = color;
        this.colorCode = colorCode;
        this.size = size;
        this.discountPrice = discountPrice;
        this.price = price;
        this.description = description;
    }

    public Item(String category, String color, String size, int amount) {
        this.category = category;
        this.color = color;
        this.size = size;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount(){
        return amount;
    }
}
