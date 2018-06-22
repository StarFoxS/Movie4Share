package com.example.star.movie4share.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Star on 2018/6/22.
 */
@Entity
public class OrderProduct {

    @Id (autoincrement = true)
    private long id;
    private long productId;
    private int productNum;
    private String memo;

    @Generated(hash = 2107683710)
    public OrderProduct(long id, long productId, int productNum, String memo) {
        this.id = id;
        this.productId = productId;
        this.productNum = productNum;
        this.memo = memo;
    }

    @Generated(hash = 1818552344)
    public OrderProduct() {
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
