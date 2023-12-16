package com.first.coffeeorderappone.Model;

public class CoffeeModel {

    String coffeeid;
    String coffeeName;
    int quantity, price;

    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeid, String coffeeName, int quantity, int price) {
        this.coffeeid = coffeeid;
        this.coffeeName = coffeeName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
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
}
