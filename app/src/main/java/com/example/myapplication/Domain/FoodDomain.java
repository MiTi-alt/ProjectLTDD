package com.example.myapplication.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private int id;
    private String title;
    private String pic;
    private String description;
    private Double fee;
    private int numberInCart;
    private int idLoaiSp;

    public FoodDomain(int id, String title, String pic, String description, Double fee, int numberInCart, int idLoaiSp) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.numberInCart = numberInCart;
        this.idLoaiSp = idLoaiSp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public int getIdLoaiSp() {
        return idLoaiSp;
    }

    public void setIdLoaiSp(int idLoaiSp) {
        this.idLoaiSp = idLoaiSp;
    }
}
