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

    public static Movie4ShareApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
    //    dbAddInit();
    //    deleteDb();
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
        Product nProduct0 = new Product(0, "警察故事", 10.0, "jingchagushi", "jcgs",
                "0", "0", 3, 3, 1);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct0);

        Product nProduct1 = new Product(1, "盗梦空间", 5.0, "daomengkongjian", "dmkj",
                "1", "1", 1, 1, 0);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct1);
    }

    public void deleteDb(){
        DaoMaster.dropAllTables(mDaoMaster.getDatabase(),true);
        DaoMaster.createAllTables(mDaoMaster.getDatabase(),true);
    }
}
