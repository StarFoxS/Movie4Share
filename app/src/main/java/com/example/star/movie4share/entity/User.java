package com.example.star.movie4share.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Star on 2018/6/16.
 */

@Entity
public class User {
    @Id
    private long id;
    @Unique
    private String customId;
    private String password;
    private int showpw;
    private String email;
    private String name;
    private String phoneNum;
    private String imgUrl;

    @Generated(hash = 675441127)
    public User(long id, String customId, String password, int showpw, String email,
            String name, String phoneNum, String imgUrl) {
        this.id = id;
        this.customId = customId;
        this.password = password;
        this.showpw = showpw;
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.imgUrl = imgUrl;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCustomId() {
        return this.customId;
    }
    public void setCustomId(String customId) {
        this.customId = customId;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNum() {
        return this.phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getImgUrl() {
        return this.imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public int getShowpw() {
        return this.showpw;
    }
    public void setShowpw(int showpw) {
        this.showpw = showpw;
    }

}
