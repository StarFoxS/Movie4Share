package com.example.star.movie4share;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.star.movie4share.dao.DaoMaster;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.entity.Order;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.example.star.movie4share.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2018/6/13.
 */

public class Movie4ShareApplication extends Application {
    private static Context context = null;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    // 记录用户状态与信息，默认为空，可选user/seller/admin
    public static String loginStatus = "";
    public static String password = "";
    public static long userId = -1;
    // 记录本用户的vip和优惠券信息
    public static double vip = -1;
    public static double couponTotal = -1;
    public static double couponMinus = -1;

    //记录用户下订单时的收货人、地址、电话
    public static String orderName = "";
    public static String orderAddr = "";
    public static String orderPhone = "";

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static double getVip() {
        return vip;
    }

    public static void setVip(double vip) {
        Movie4ShareApplication.vip = vip;
    }

    public static double getCouponTotal() {
        return couponTotal;
    }

    public static void setCouponTotal(double couponTotal) {
        Movie4ShareApplication.couponTotal = couponTotal;
    }

    public static double getCouponMinus() {
        return couponMinus;
    }

    public static void setCouponMinus(double couponMinus) {
        Movie4ShareApplication.couponMinus = couponMinus;
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

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
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

    public DaoMaster getDaoMaster() { return mDaoMaster; }

    public SQLiteDatabase getDb(){
        return db;
    }

    public void dbAddInit(){
        //Long id, String productName, double price, String description,String shortDescription,
        // String url, String urlDescription, int stockNum, int limitNum,int boughtNum

        //List<Product> nProductList = new ArrayList<>();
        Product nProduct0 = new Product(0, "警察故事", "action", 10.0, "jingchagushi", "2013年成龙主演动作电影",
                "http://img31.mtime.cn/pi/2013/12/09/031418.40663979_1000X1000.jpg", "https://movie.douban.com/subject/20266753/",
                3, 3, 1);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct0);

        Product nProduct1 = new Product(1, "盗梦空间", "fun", 5.0, "daomengkongjian", "dmkj",
                "https://img3.doubanio.com/view/photo/l/public/p513344864.webp", "1", 0, 0, 0);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct1);

        Product nProduct2 = new Product(2, "战狼2", "action", 12.0, "zhanlang2", "2017年吴京主演动作电影",
                "https://img3.doubanio.com/view/photo/l/public/p2494701965.webp", "https://movie.douban.com/subject/26363254/",
                5, 1, 0);
        //nProductList.add(nProduct);
        getDaoSession().insert(nProduct2);

        Product nProduct3 = new Product(3, "何以笙箫默", "love", 13.5, "heyishengxiaomo", "2015年黄晓明、杨幂主演爱情电影",
                "https://img1.doubanio.com/view/photo/l/public/p2239863767.webp", "https://movie.douban.com/subject/26259634/",
                100, 100, 0);
        getDaoSession().insert(nProduct3);

        Product nProduct4 = new Product(4, "喜剧之王", "fun", 13.5, "xijuzhiwang", "1999年周星驰、张柏芝主演喜剧电影",
                "https://img3.doubanio.com/view/photo/l/public/p1043597424.webp", "https://movie.douban.com/subject/1302425/",
                100, 100, 0);
        getDaoSession().insert(nProduct4);

        Product nProduct5 = new Product(5, "我不是药神", "fun", 99, "wobushiyaoshen", "2018年徐峥主演喜剧电影，票房冠军！",
                "https://img3.doubanio.com/view/photo/l/public/p2519070834.webp", "https://movie.douban.com/subject/26752088/",
                10000, 100, 0);
        getDaoSession().insert(nProduct5);

        Product nProduct6 = new Product(6, "霸王别姬", "love", 200.1, "bawangbieji", "1993年张国荣、张丰毅主演爱情悲剧电影",
                "https://img3.doubanio.com/view/photo/l/public/p1910813120.webp", "https://movie.douban.com/subject/1291546/",
                100, 100, 0);
        getDaoSession().insert(nProduct6);

        Product nProduct7 = new Product(7, "黑客帝国", "action", 1.0, "heikediguo", "1999年基努李维斯主演动作电影",
                "https://img1.doubanio.com/view/photo/l/public/p451926968.webp", "https://movie.douban.com/subject/1291843/",
                100, 100, 0);
        getDaoSession().insert(nProduct7);

        Product nProduct8 = new Product(8, "3idiots", "fun", 1.0, "3idiots", "2009年阿米尔汗主演喜剧电影",
                "https://img3.doubanio.com/view/photo/l/public/p579729551.webp", "https://movie.douban.com/subject/3793023/",
                100, 100, 0);
        getDaoSession().insert(nProduct8);



        ShopCartProduct nShopCartProduct0 = new ShopCartProduct(0, "战狼2", "action", 12.0, 1,
                "https://img3.doubanio.com/view/photo/l/public/p2494701965.webp", 5);
        getDaoSession().insert(nShopCartProduct0);

        Order nOrder0 = new Order(50, 0, "2018.06.11", "已发货", "20180611010105", 20, 2,
                "http://www.dgpinglong.com/uploadfile/image/20170328/20170328140349_1967832161.jpg", 0, -1, -1, 2, -1, -1);
        getDaoSession().insert(nOrder0);

        Order nOrder1 = new Order(51, 0, "2018.06.12", "未发货", "20180612020206", 175.5, 13,
                "http://pic36.photophoto.cn/20150812/0037037002912269_b.jpg", 3, 4, -1, 9, 4, -1);
        getDaoSession().insert(nOrder1);

        User nUser0 = new User(0, "starfei@cmbchina.com", "1", 0, "上海市来安路686号", "文媛", "58634",
                "https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1468487353.74.webp", 0.6, -1, -1);
        getDaoSession().insert(nUser0);

        User nUser1 = new User(1, "test@test.com", "2", 0, "测试路404号", "test", "",
                "https://www.nvsay.com/uploads/allimg/170816/38-1FQ610414Qc.jpg", 1, 30, 10);
        getDaoSession().insert(nUser1);

        User nUser2 = new User(2, "3@3", "3", 0, "火星", "test", "38834600*58634",
                "https://b-ssl.duitang.com/uploads/item/201611/19/20161119111625_WaxdZ.thumb.700_0.jpeg", 0.9, 30, 10);
        getDaoSession().insert(nUser2);

    }

    public void deleteDb(){
        DaoMaster.dropAllTables(mDaoMaster.getDatabase(),true);
        DaoMaster.createAllTables(mDaoMaster.getDatabase(),true);
    }
}
