package edu.dhbw.andobjviewer.models;

/**
 * Created by Edwin on 23/11/2016.
 */

public class Product {
    private String title;
    private int price;
    private int image;

    public Product(){
    }

    public Product(String title, int price, int image){
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
