package com.example.star.movie4share.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Star on 2018/6/16.
 */

@Entity
public class User {
    @Id
    private long id;
    private int customId;
    private String password;
    private String email;
    private String name;
    private String phoneNum;

    @Generated(hash = 2070635033)
    public User(long id, int customId, String password, String email, String name,
            String phoneNum) {
        this.id = id;
        this.customId = customId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
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
    public int getCustomId() {
        return this.customId;
    }
    public void setCustomId(int customId) {
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

}
