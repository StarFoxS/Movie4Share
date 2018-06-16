package com.example.star.movie4share.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Star on 2018/6/14.
 */

@Entity
public class Product implements Serializable {

    @Id (autoincrement = true)
    private Long id;

    private String productName;
    private double price;
    //private double pastPrice;
    private String description;
    private String shortDescription;
    @Unique
    private String url;
    private String urlDescription;
    private int stockNum;
    private int limitNum;
    private int boughtNum;

    static final long serialVersionUID = 42L;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getBoughtNum() {
        return boughtNum;
    }

    public void setBoughtNum(int boughtNum) {
        this.boughtNum = boughtNum;
    }

    public Product(Long ID, String name, double price, int stock, String url, String description,
                   String shortDescription, String urlDescription) {
        this.id = ID;
        this.productName = name;
        this.price = price;
        this.stockNum = stock;
        this.url = url;
        this.description = description;
        this.shortDescription = shortDescription;
        this.urlDescription = urlDescription;
    }

    public Product(Long ID, String name, double price, int stock, String url, String description,
                   String shortDescription, String urlDescription, int limit, int alreadyBought){
        this.id = ID;
        this.productName = name;
        this.price = price;
        this.stockNum = stock;
        this.url = url;
        this.description = description;
        this.shortDescription = shortDescription;
        this.urlDescription = urlDescription;
        this.limitNum = limit;
        this.boughtNum = alreadyBought;
    }

    @Generated(hash = 1855550246)
    public Product(Long id, String productName, double price, String description,
            String shortDescription, String url, String urlDescription, int stockNum, int limitNum,
            int boughtNum) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.url = url;
        this.urlDescription = urlDescription;
        this.stockNum = stockNum;
        this.limitNum = limitNum;
        this.boughtNum = boughtNum;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }
}
