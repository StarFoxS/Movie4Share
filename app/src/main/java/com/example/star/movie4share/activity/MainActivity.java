package com.example.star.movie4share.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.fragment.FindNew;
import com.example.star.movie4share.fragment.HomePage;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomePage.OnFragmentInteractionListener, FindNew.OnFragmentInteractionListener{

    private View tab_home,tab_find_new,tab_classify,tab_shop_cart,tab_profile;
    private Fragment[] mFragments;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    public void init(){
        tab_home = findViewById(R.id.tab_home);
        tab_find_new = findViewById(R.id.tab_find_new);
        tab_classify = findViewById(R.id.tab_classify);
        tab_shop_cart = findViewById(R.id.tab_shop_cart);
        tab_profile = findViewById(R.id.tab_profile);

        tab_home.setOnClickListener(new tabOnClickListener(0));
        tab_find_new.setOnClickListener(new tabOnClickListener(1));
        tab_classify.setOnClickListener(new tabOnClickListener(2));
        tab_shop_cart.setOnClickListener(new tabOnClickListener(3));
        tab_profile.setOnClickListener(new tabOnClickListener(4));

        mFragments = new Fragment[5];
        fragmentManager = this.getSupportFragmentManager();

        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_homepage);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_findnew);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_classify);
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

    public class tabOnClickListener implements View.OnClickListener{
        private int index = 0;

        public tabOnClickListener(int i){ index = i; }

        @Override
        public void onClick(View v){
            ((TextView) tab_home.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_find_new.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_classify.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_shop_cart.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);
            ((TextView) tab_profile.findViewById(R.id.tab_text)).setTextColor(Color.GRAY);

            ((ImageView) tab_home.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_find_new.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_classify.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
            ((ImageView) tab_shop_cart.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_1);
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
                    ((ImageView) tab_find_new.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
                    break;
                case 2:
                    ((TextView) tab_classify.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_classify.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
                    break;
                case 3:
                    ((TextView) tab_shop_cart.findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
                    ((ImageView) tab_shop_cart.findViewById(R.id.tab_icon1)).setImageResource(R.drawable.home_0);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bought_list) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_profile) {

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
