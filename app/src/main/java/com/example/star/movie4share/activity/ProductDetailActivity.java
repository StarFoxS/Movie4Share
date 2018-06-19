package com.example.star.movie4share.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.squareup.picasso.Picasso;


/**
 * Created by Star on 2018/6/16.
 */

public class ProductDetailActivity extends Activity {

    public static ProductDetailActivity mProductDetailActivity = null;

    private static final int QUERY_YES = 0x100;
    private static final int QUERY_NO = 0x101;

    private static boolean isQuery = false;
    private Product mProduct;
    private long productId;

    private double pastPrice = 10000;

    private WebView mTVDetails;
    private EditText mTVNumber;
    private TextView mTVPopDetails;
    private TextView mTVPrice;
    private TextView mTVTopPrice;
    private TextView mTVPopCategory;
    private TextView mProductCaption;
    private TextView StockNum;
    private TextView pastPriceText;
    private TextView limitTextView;

    // “加入购物车”按钮
    private Button mBtnAddToCart;
    private Button mBtnMinute;
    private Button mBtnPlus;

    private View mPop;

    private PopupWindow mPopupWindow;

    private ImageView mImgDetails;
    private ImageView mImgClose;
    private ImageView mImgIcon;

    //购物车里“确定”按钮
    private Button mBtnOK;

    private int numOfCouldBuy = 10000;
    private int originalLimit = 0;

    private Button cartBtn;

    private boolean isPopOpened;

    private int number = 1;

    @Override
    public void onCreate(Bundle savedInstanceData){
        super.onCreate(savedInstanceData);
        setContentView(R.layout.activity_detail_product);

        mProductDetailActivity = this;

        Intent intent = this.getIntent();
        productId = (long) intent.getSerializableExtra("productId");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (LoginActivity.getLogin_status() == 1){
                    mProduct = getProductLogin();
                } else {
                    mProduct = getProductLogoff();
                }
                Message msg = new Message();
                msg.what = 5566;
                handler.sendMessage(msg);
            }
        }).start();

        initViews();

        pastPriceText = (TextView) findViewById(R.id.tv_activity_product_details_past_price);
        limitTextView = (TextView) findViewById(R.id.product_limit);

        cartPopUpListener();
        cartAddDaoListener();
        cartCloseListener();
        cartNumChangeListener();

        cartBtn = (Button) findViewById(R.id.btn_activity_product_details_buy_now);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ShopCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private Product getProductLogin() {

        ProductDao dao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
        mProduct = dao.load(productId);
        return mProduct;

    }

    private Product getProductLogoff() {

        ProductDao dao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
        mProduct = dao.load(productId);
        return mProduct;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case QUERY_YES:
                    int number = (int) msg.obj;
                    updateDatabase(number);
                    break;
                case QUERY_NO:
                    insert2Sqlite();
                    break;
                case 5566:
                    initData();
                    break;
            }
        }
    };

    public void initData(){
        String category = "";
        Picasso.get().load(mProduct.getUrl()).into(mImgDetails);
        Picasso.get().load(mProduct.getUrl()).into(mImgIcon);
        pastPriceText.setText("价格: " + pastPrice);
        if (originalLimit != 0){
            limitTextView.setText("限量: " + originalLimit);
        } else {
            limitTextView.setText("不限量");
        }

        mProductCaption.setText(mProduct.getProductName());
        mTVDetails.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings mWebSettings = mTVDetails.getSettings();
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(false);
        mTVDetails.loadUrl(mProduct.getUrlDescription());
        mTVTopPrice.setText("价格: " + mProduct.getPrice());
        mTVPrice.setText("价格: " + mProduct.getPrice());
        mTVPopDetails.setText(mProduct.getShortDescription());
        StockNum.setText("库存: " + mProduct.getStockNum());
        mTVPopCategory.setText(category);
    }

    private void initViews() {
        mTVDetails = (WebView) findViewById(R.id.tv_activity_product_details_details);
        mBtnAddToCart = (Button) findViewById(R.id.btn_activity_product_details_add_to_cart);
        mImgDetails = (ImageView) findViewById(R.id.img_activity_product);
        mTVTopPrice = (TextView) findViewById(R.id.tv_activity_product_details_price);
        mProductCaption = (TextView) findViewById(R.id.detail_product_name);
        mPop = LayoutInflater.from(this).inflate(R.layout.add_to_cart, null);
        mImgIcon = (ImageView) mPop.findViewById(R.id.img_pop_icon);
        mBtnOK = (Button) mPop.findViewById(R.id.btn_pop_ok);
        mBtnMinute = (Button) mPop.findViewById(R.id.btn_pop_minute);
        mBtnPlus = (Button) mPop.findViewById(R.id.btn_pop_plus);
        mImgClose = (ImageView) mPop.findViewById(R.id.img_pop_close);
        mTVNumber = (EditText) mPop.findViewById(R.id.tv_pop_number);
        mTVPopDetails = (TextView) mPop.findViewById(R.id.tv_pop_details);
        mTVPrice = (TextView) mPop.findViewById(R.id.tv_pop_price);
        mTVPopCategory = (TextView) mPop.findViewById(R.id.tv_pop_category);

        mTVNumber.setText("1");

        mTVNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String num = mTVNumber.getText().toString();
                if (!num.equals("")){
                    int numberTmp = Integer.valueOf(num);
                    if (numberTmp > mProduct.getStockNum()){
                        Toast.makeText(ProductDetailActivity.this,"库存只有"+mProduct.getStockNum(),Toast.LENGTH_SHORT).show();
                        number = mProduct.getStockNum();
                        mTVNumber.setText(number+"");
                    }else{
                        if (numberTmp > numOfCouldBuy){
                            Toast.makeText(ProductDetailActivity.this,"限购只剩"+numOfCouldBuy+"件可以购买",Toast.LENGTH_SHORT).show();
                            number = numOfCouldBuy;
                            mTVNumber.setText(number+"");
                        }
                        else
                            number = numberTmp;
                    }
                }else
                    number = 1;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPop.setFocusable(true);
        int PopHeight = getWindow().getAttributes().height;
        mPopupWindow = new PopupWindow(mPop, getWindow().getAttributes().width, PopHeight*2);
        mPopupWindow.setFocusable(true);
        StockNum = (TextView) findViewById(R.id.tv_activity_product_details_stock);
        pastPriceText = (TextView) findViewById(R.id.tv_activity_product_details_past_price);
    }

    private void cartPopUpListener() {

        /*
         *  加入购物车窗口的弹出
         *  TODO: setAnimationStyle中写一个style名为PopUpWindowStyle使其缓慢上浮弹出
         */
        mBtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPopOpened == false) {
                    final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.alpha = 0.3f;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().setAttributes(layoutParams);
                        }
                    }, 500);

//                    mPopupWindow.setAnimationStyle(R.style.PopUpWindowStyle);
                    mPopupWindow.showAtLocation(mImgDetails, Gravity.BOTTOM, 0, 0);
                    isPopOpened = true;
                }
            }
        });
    }

        /*
         * 商品添加到购物车，更新购物车库ShopCartProduct
         */
    private void cartAddDaoListener() {
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 保存产品信息到数据库
                String num = mTVNumber.getText().toString();
                if (Movie4ShareApplication.loginStatus == "user") {
                    if (num.length() == 0) {
                        Toast.makeText(getApplication(), "商品数量不能为0！", Toast.LENGTH_SHORT).show();
                    } else {
                        int num2 = Integer.valueOf(num);
                        if (num2 == 0) {
                            Toast.makeText(getApplication(), "商品数量不能为0！", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: 增加购物车库物品数量
                            // addToCart();

                            if (isPopOpened == true) {
                                final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                                layoutParams.alpha = 1.0f;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getWindow().setAttributes(layoutParams);
                                    }
                                }, 500);
                                mPopupWindow.dismiss();
                                isPopOpened = false;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplication(), "请先登录", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
     * 关闭购物车弹窗的监听
     */
    private void cartCloseListener(){
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPopOpened == true){
                    final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.alpha = 1.0f;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().setAttributes(layoutParams);
                        }
                    }, 500);
                    mPopupWindow.dismiss();
                    isPopOpened = false;
                }
            }
        });
    }

    /*
     * 购物车数量调整的监听，增加/减少两个按键
     */
    private void cartNumChangeListener() {
        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Movie4ShareApplication.loginStatus == "user"){
                    String changeNum = mTVNumber.getText().toString();
                    number = Integer.valueOf(changeNum);
                    if (number < mProduct.getStockNum()) {
                        number ++;
                    }
                    mTVNumber.setText(number + "");
                } else {
                    Toast.makeText(ProductDetailActivity.this,"您没有购买的权限！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Movie4ShareApplication.loginStatus != "user"){
                    Toast.makeText(ProductDetailActivity.this,"您没有购买的权限！",Toast.LENGTH_SHORT).show();
                }
                else {
                    String num = mTVNumber.getText().toString();
                    number = Integer.valueOf(num);
                    if (number > 0) {
                        number --;
                        mTVNumber.setText(number + "");
                    }
                }
            }
        });
    }

    /*
     *  Insert into database
     */
    public void insert2Sqlite(){
        //long id, String name, String category, double price,
        //int number, String imgUrl, int stock
        ShopCartProduct nShopCartProduct = new ShopCartProduct(0, mProduct.getProductName(), "anyCategory",
                mProduct.getPrice(), number, mProduct.getUrl(), mProduct.getStockNum());
        Movie4ShareApplication.getInstances().getDaoSession().insert(nShopCartProduct);
    }

    /*
     *  Update the database
     */
    private void updateDatabase(int cartNumber){
        if (cartNumber + number <= numOfCouldBuy){
            ShopCartProduct nShopCartProduct = new ShopCartProduct(0, mProduct.getProductName(), "anyCategory",
                    mProduct.getPrice(), number + cartNumber, mProduct.getUrl(), mProduct.getStockNum());
            Movie4ShareApplication.getInstances().getDaoSession().update(nShopCartProduct);
            Toast.makeText(getApplication(), "您已经成功添加到购物车~", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(ProductDetailActivity.this,"添加失败，购物车中已有此商品" + cartNumber + "件，您还可再添加"
                     + (numOfCouldBuy-cartNumber) + "件商品", Toast.LENGTH_SHORT).show();
        }
    }
}
