package com.example.star.movie4share.entity;

import com.example.star.movie4share.fragment.ShopCart;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Star on 2018/6/16.
 */

@Entity
public class ShopCartProduct implements Serializable {

    @Id
    private long id;
    private String name;
    private String category;
    private double price;
    private int number ;
    private String imgUrl;
    private int stock;

    static final long serialVersionUID = 42L;

    @Generated(hash = 2132111055)
    public ShopCartProduct(long id, String name, String category, double price,
            int number, String imgUrl, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.number = number;
        this.imgUrl = imgUrl;
        this.stock = stock;
    }

    @Generated(hash = 666397281)
    public ShopCartProduct() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
