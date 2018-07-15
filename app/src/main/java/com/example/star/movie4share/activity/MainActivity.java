package com.example.star.movie4share.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.fragment.FindNew;
import com.example.star.movie4share.fragment.HomePage;
import com.example.star.movie4share.fragment.Profile;
import com.example.star.movie4share.fragment.ShopCart;
import com.example.star.movie4share.fragment.ViewOrder;

/*
 * 用户User的主界面，下分5个fragments
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomePage.OnFragmentInteractionListener, FindNew.OnFragmentInteractionListener,
        ViewOrder.OnFragmentInteractionListener, ShopCart.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener {

    private View tab_home,tab_find_new,tab_view_order,tab_shop_cart,tab_profile;
    private Fragment[] mFragments;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabListener();



        init();
    }

    public void fabListener(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Movie4ShareApplication.couponTotal == -1){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("领取优惠券");
                    builder.setIcon(R.drawable.coupon_icon);
                    String str = "成功领取一张【满30减10】优惠券！";
                    final TextView textView = new TextView(MainActivity.this);
                    textView.setText(str);
                    textView.setTextSize(24);
                    builder.setView(textView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("去购物", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();

                    Movie4ShareApplication.couponTotal = 30;
                    Movie4ShareApplication.couponMinus = 10;
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("领取优惠券");
                    builder.setIcon(R.drawable.coupon_icon);
                    String str = "领取失败……您已有一张【满30减10】优惠券！";
                    final TextView textView = new TextView(MainActivity.this);
                    textView.setText(str);
                    textView.setTextSize(24);
                    builder.setView(textView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });
    }

    /*
     * Initialize the activity
     */
    public void init(){
        tab_home = findViewById(R.id.tab_home);
        tab_find_new = findViewById(R.id.tab_find_new);
        tab_view_order = findViewById(R.id.tab_view_order);
        tab_shop_cart = findViewById(R.id.tab_shop_cart);
        tab_profile = findViewById(R.id.tab_profile);

        tab_home.setOnClickListener(new tabOnClickListener(0));
        tab_find_new.setOnClickListener(new tabOnClickListener(1));
        tab_view_order.setOnClickListener(new tabOnClickListener(2));
        tab_shop_cart.setOnClickListener(new tabOnClickListener(3));
        tab_profile.setOnClickListener(new tabOnClickListener(4));

        mFragments = new Fragment[5];
        fragmentManager = this.getSupportFragmentManager();

        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_homepage);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_findnew);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_view_order);
        mFragments[3] = fragmentManager.findFragmentById(R.id.fragment_shopcart);
        mFragments[4] = fragmentManager.findFragmentById(R.id.fragment_profile);

        transaction = fragmentManager.beginTransaction()
                .hide(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .hide(mFragments[3])
                .hide(mFragments[4]);

        transaction.show(mFragments[0]).commit();

        //redDot = findViewById(R.id.redDot);
    }

    @Override
    public void onResume(){
        super.onResume();
        String getStringExtra = "";
        try {
            getStringExtra = getIntent().getStringExtra("casefragment");
            switch (getStringExtra){
                // when "查看购物车" button is clicked
                case "shopcart":
                    transaction = fragmentManager.beginTransaction()
                            .hide(mFragments[0])
                            .hide(mFragments[1])
                            .hide(mFragments[2])
                            .hide(mFragments[3])
                            .hide(mFragments[4]);
                    transaction.show(mFragments[3]).commit();
                    break;
                // when "清空购物车" button is clicked
                case "refreshshopcart":
                    transaction = fragmentManager.beginTransaction()
                            .replace(R.id.fragment_shopcart, mFragments[3])
                            .hide(mFragments[0])
                            .hide(mFragments[1])
                            .hide(mFragments[2])
                            .hide(mFragments[4]).show(mFragments[3]);
                    transaction.commit();
                    break;
                // when "去看看"(付款后) / 我的订单 is clicked
                case "orderlist":
                    transaction = fragmentManager.beginTransaction()
                            .replace(R.id.fragment_view_order, mFragments[2])
                            .hide(mFragments[0])
                            .hide(mFragments[1])
                            .hide(mFragments[3])
                            .hide(mFragments[4]);
                    transaction.show(mFragments[2]).commit();
                    break;
            }

        } catch (Exception e) {

        }
    }

    /*
     * 底部标签栏listener
     * 共有五个：首页(搜索)/发现(分类)/订单/购物车/我的资料
     * 点击时选中的tab颜色变深
     */
    public class tabOnClickListener implements View.OnClickListener{
        private int index = 0;

        public tabOnClickListener(int i){ index = i; }

        @Override
        public void onClick(View v){
            ((TextView) tab_home.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_find_new.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_view_order.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_shop_cart.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_profile.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);

            ((ImageView) tab_home.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_find_new.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.classify_1);
            ((ImageView) tab_view_order.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.order_1);
            ((ImageView) tab_shop_cart.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.shopcart_1);
            ((ImageView) tab_profile.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.user_1);

            transaction = fragmentManager.beginTransaction()
                    .hide(mFragments[0])
                    .hide(mFragments[1])
                    .hide(mFragments[2])
                    .hide(mFragments[3])
                    .hide(mFragments[4]);

            switch (index) {
                case 0:
                    ((TextView) tab_home.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_home.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
                    break;
                case 1:
                    ((TextView) tab_find_new.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_find_new.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.classify_0);
                    break;
                case 2:
                    ((TextView) tab_view_order.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_view_order.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.order_0);
                    break;
                case 3:
                    ((TextView) tab_shop_cart.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_shop_cart.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.shopcart_0);
                    break;
                case 4:
                    ((TextView) tab_profile.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_profile.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.user_0);
                    break;
            }

            transaction.show(mFragments[index]).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * 左侧划拉drawer的选择器
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order_list) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("casefragment","orderlist");
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.nav_coupon) {
            //TODO: step into coupon

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("订单详情");
            builder.setIcon(R.drawable.coupon_icon);
            String str;
            if (Movie4ShareApplication.vip != 1.0) {
                str = "VIP折扣:" + Movie4ShareApplication.vip * 10 + "折";
            } else {
                str = "无VIP折扣";
            }
            if (Movie4ShareApplication.couponTotal != -1) {
                str += "  &  满" + Movie4ShareApplication.couponTotal + "减" + Movie4ShareApplication.couponMinus
                        + "券一张";
            }
            final TextView textView = new TextView(this);
            textView.setText(str);
            textView.setTextSize(18);
            builder.setView(textView);
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();


        } else if (id == R.id.nav_profile) {
            transaction = fragmentManager.beginTransaction()
                    .hide(mFragments[0])
                    .hide(mFragments[1])
                    .hide(mFragments[2])
                    .hide(mFragments[3])
                    .hide(mFragments[4]);

            transaction.show(mFragments[4]).commit();

        } else if (id == R.id.nav_logoff) {
            Movie4ShareApplication.loginStatus = "";
            Movie4ShareApplication.password = "";
            Movie4ShareApplication.userId = -1;
            Movie4ShareApplication.vip = 1.0;
            Movie4ShareApplication.couponTotal = -1;
            Movie4ShareApplication.couponMinus = -1;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
