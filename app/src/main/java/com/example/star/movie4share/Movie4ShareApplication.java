package com.example.star.movie4share;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.star.movie4share.dao.DaoMaster;
import com.example.star.movie4share.dao.DaoSession;

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
}
