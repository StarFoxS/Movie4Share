package com.example.star.movie4share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.bean.Product;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by Star on 2018/6/15.
 */

public class MovieTypeAction extends Activity{
    private ArrayList<Product> movieList = new ArrayList<>();
    private XRecyclerView mXRecycleView;
    private int times = 0;
    private String type;
    //private Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");

        setContentView(R.layout.activity_movie_list_action);
        mXRecycleView = (XRecyclerView) this.findViewById(R.id.x_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mXRecycleView.setLayoutManager(layoutManager);
        mXRecycleView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecycleView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        TextView title = (TextView)this.findViewById(R.id.product_list_title);
        switch (type){
            case "action":
                title.setText("动作片");
                break;
            case "love":
                title.setText("爱情片");
                break;
            case "fun":
                title.setText("喜剧片");
                break;
        }

        mXRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                times = 0;
                movieList.clear();

            }

            @Override
            public void onLoadMore() {
                times ++;

            }
        });
    }
}
