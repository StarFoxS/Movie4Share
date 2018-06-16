package com.example.star.movie4share.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
    private Button mBtnAddToCart;
    private Button mBtnMinute;
    private Button mBtnPlus;

    private View mPop;

    private PopupWindow mPopupWindow;

    private ImageView mImgDetails;
    private ImageView mImgClose;
    private ImageView mImgIcon;

    private Button mBtnOK;

    private int numOfCouldBuy = 0;
    private int originalLimit = 0;

    private Button cartBtn;

    private boolean isPopOpened;

    private int number = 1;

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
//                    initData();
                    break;
            }
        }
    };

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
                    //mProduct = getProductLogin();
                } else {
                    //mProduct = getProductLogoff();
                }
                Message msg = new Message();
                msg.what = 5566;
                handler.sendMessage(msg);
            }
        }).start();

        //initViews

        pastPriceText = (TextView) findViewById(R.id.tv_activity_product_details_past_price);
        limitTextView = (TextView) findViewById(R.id.product_limit);

        //addListeners

        cartBtn = (Button) findViewById(R.id.btn_activity_product_details_buy_now);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ShopCartActivity.class);
                startActivity(intent);
            }
        });
    }

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
