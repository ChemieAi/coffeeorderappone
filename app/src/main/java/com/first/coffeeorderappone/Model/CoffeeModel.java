package com.first.coffeeorderappone.Model;

import com.google.firebase.firestore.DocumentId;

public class CoffeeModel {

    @DocumentId
    String coffeeid;
    String description, imageURL, coffeename;
    int price;

    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeid, String description, String imageURL, String coffeename, int price) {
        this.coffeeid = coffeeid;
        this.description = description;
        this.imageURL = imageURL;
        this.coffeename = coffeename;
        this.price = price;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCoffeename() {
        return coffeename;
    }

    public void setCoffeename(String coffeename) {
        this.coffeename = coffeename;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CoffeeModel{" +
                "coffeeid='" + coffeeid + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", coffeename='" + coffeename + '\'' +
                ", price=" + price +
                '}';
    }
}
