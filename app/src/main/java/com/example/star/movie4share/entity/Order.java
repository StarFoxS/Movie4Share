package com.example.star.movie4share.entity;

import android.text.format.Time;

import com.example.star.movie4share.fragment.ShopCart;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.dao.OrderProductDao;
import com.example.star.movie4share.dao.ReceiverDao;
import org.greenrobot.greendao.annotation.NotNull;
import com.example.star.movie4share.dao.OrderDao;

/**
 * Created by Star on 2018/6/22.
 */

@Entity
public class Order {

    @Id (autoincrement = true)
    private long id;

    private long userId;
//    private Time time; //Greendao似乎不支持Time类
    private String time;
    @ToOne (joinProperty = "userId")
    private Receiver receiver;
    // status = "待付款"/"未发货"/"已发货"/"已签收"
    private String status;
    private String serialNum;
    private double price;
    private int productNum;
    private String imgUrl;

    private long id0, id1, id2;
    private int num0, num1, num2;

    @ToMany (referencedJoinProperty = "productId")
    private List<OrderProduct> orderProduct;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 949219203)
    private transient OrderDao myDao;

    @Generated(hash = 118553546)
    private transient Long receiver__resolvedKey;

    @Generated(hash = 552014011)
    public Order(long id, long userId, String time, String status, String serialNum, double price,
            int productNum, String imgUrl, long id0, long id1, long id2, int num0, int num1, int num2) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.status = status;
        this.serialNum = serialNum;
        this.price = price;
        this.productNum = productNum;
        this.imgUrl = imgUrl;
        this.id0 = id0;
        this.id1 = id1;
        this.id2 = id2;
        this.num0 = num0;
        this.num1 = num1;
        this.num2 = num2;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String  getTime() {
        return time;
    }

    public void setTime(String  time) {
        this.time = time;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 711402062)
    public Receiver getReceiver() {
        long __key = this.userId;
        if (receiver__resolvedKey == null || !receiver__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReceiverDao targetDao = daoSession.getReceiverDao();
            Receiver receiverNew = targetDao.load(__key);
            synchronized (this) {
                receiver = receiverNew;
                receiver__resolvedKey = __key;
            }
        }
        return receiver;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1484420449)
    public void setReceiver(@NotNull Receiver receiver) {
        if (receiver == null) {
            throw new DaoException(
                    "To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.receiver = receiver;
            userId = receiver.getId();
            receiver__resolvedKey = userId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 993185050)
    public List<OrderProduct> getOrderProduct() {
        if (orderProduct == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderProductDao targetDao = daoSession.getOrderProductDao();
            List<OrderProduct> orderProductNew = targetDao
                    ._queryOrder_OrderProduct(id);
            synchronized (this) {
                if (orderProduct == null) {
                    orderProduct = orderProductNew;
                }
            }
        }
        return orderProduct;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1144882779)
    public synchronized void resetOrderProduct() {
        orderProduct = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 965731666)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }

    public long getId0() {
        return this.id0;
    }

    public void setId0(long id0) {
        this.id0 = id0;
    }

    public long getId1() {
        return this.id1;
    }

    public void setId1(long id1) {
        this.id1 = id1;
    }

    public long getId2() {
        return this.id2;
    }

    public void setId2(long id2) {
        this.id2 = id2;
    }

    public int getNum0() {
        return this.num0;
    }

    public void setNum0(int num0) {
        this.num0 = num0;
    }

    public int getNum1() {
        return this.num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return this.num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }
}
