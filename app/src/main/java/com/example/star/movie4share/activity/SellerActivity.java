package com.example.star.movie4share.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.entity.User;
import com.example.star.movie4share.fragment.FindNew;
import com.example.star.movie4share.fragment.Post;
import com.example.star.movie4share.fragment.ViewOrder;

import java.util.ArrayList;

/**
 * Created by Star on 2018/6/25.
 */

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FindNew.OnFragmentInteractionListener, Post.OnFragmentInteractionListener, ViewOrder.OnFragmentInteractionListener {

    private View tab_classify, tab_post, tab_order;
    private Fragment[] mFragments;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seller_main);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:显示drawer
                if (drawer.isDrawerOpen(navigationView)){
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });

        init();
    }


    public void init(){

        tab_classify = findViewById(R.id.seller_tab_classify);
        tab_post = findViewById(R.id.seller_tab_post);
        tab_order = findViewById(R.id.seller_tab_order);

        tab_classify.setOnClickListener(new SellerActivity.tabOnClickListener(0));
        tab_post.setOnClickListener(new SellerActivity.tabOnClickListener(1));
        tab_order.setOnClickListener(new SellerActivity.tabOnClickListener(2));

        mFragments = new Fragment[3];
        fragmentManager = this.getSupportFragmentManager();

        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_classify);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_post);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_order);


        transaction = fragmentManager.beginTransaction()
                .hide(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2]);

        transaction.show(mFragments[0]).commit();
    }


    public class tabOnClickListener implements View.OnClickListener{
        private int index = 0;

        public tabOnClickListener(int i){ index = i; }

        @Override
        public void onClick(View v){
            ((TextView) tab_classify.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_post.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_order.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);

            ((ImageView) tab_classify.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_post.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_order.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);

            transaction = fragmentManager.beginTransaction()
                    .hide(mFragments[0])
                    .hide(mFragments[1])
                    .hide(mFragments[2]);

            switch (index) {
                case 0:
                    ((TextView) tab_classify.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_classify.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
                    break;
                case 1:
                    ((TextView) tab_post.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_post.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.add_0);
                    break;
                case 2:
                    ((TextView) tab_order.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_order.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order_list) {

        } else if (id == R.id.nav_coupon) {
            //TODO: step into coupon
        } else if (id == R.id.nav_profile) {
            //TODO: step into fragment[4] profile
        } else if (id == R.id.nav_logoff) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
