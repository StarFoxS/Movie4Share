package com.example.star.movie4share;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.star.movie4share.dao.DaoMaster;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2018/6/13.
 */

public class Movie4ShareApplication extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    // 记录用户状态与信息，默认为空，可选user/seller/admin
    public static String loginStatus = "user";

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public static Movie4ShareApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();

        // Delete all files in database
        deleteDb();

        //Initialize the products in database
        dbAddInit();
    }

    public static Movie4ShareApplication getInstances(){
        return instances;
    }

    private void setDatabase(){
        mHelper = new DaoMaster.DevOpenHelper(this, "product-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public void dbAddInit(){
        //Long id, String productName, double price, String description,String shortDescription,
        // String url, String urlDescription, int stockNum, int limitNum,int boughtNum

        //List<Product> nProductList = new ArrayList<>();
        Product nProduct0 = new Product(0, "警察故事2013", "action", 10.0, "jingchagushi", "2013年成龙主演动作电影",
                "http://img31.mtime.cn/pi/2013/12/09/031418.40663979_1000X1000.jpg", "https://movie.douban.com/subject/20266753/",
                3, 3, 1);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct0);

        Product nProduct1 = new Product(1, "盗梦空间", "mystery", 5.0, "daomengkongjian", "dmkj",
                "https://img3.doubanio.com/view/photo/l/public/p513344864.webp", "1", 0, 0, 0);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct1);


        Product nProduct2 = new Product(2, "战狼2", "action", 12.0, "zhanlang2", "2017年吴京主演动作电影",
                "https://img3.doubanio.com/view/photo/l/public/p2494701965.webp", "https://movie.douban.com/subject/26363254/",
                1, 1, 0);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct2);
    }

    public void deleteDb(){
        DaoMaster.dropAllTables(mDaoMaster.getDatabase(),true);
        DaoMaster.createAllTables(mDaoMaster.getDatabase(),true);
    }
}
