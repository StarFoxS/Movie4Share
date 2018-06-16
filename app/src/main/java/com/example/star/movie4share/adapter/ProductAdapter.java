package com.example.star.movie4share.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.entity.Product;

import java.util.List;

/**
 * Created by Star on 2018/6/16.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private int resourceId;
    public Product product;
    public Bitmap bitmap;

    public ProductAdapter (Context context, int textViewResourceId,
                           List<Product> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View concertView, ViewGroup parent) {
        product = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        ImageView productImage = (ImageView) view.findViewById(R.id.product_image);
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);

//        ImageLoader.getInstance().displayImage(product.getImage_url(), productImage);
        productName.setText(product.getProductName());
        productPrice.setText(String.valueOf(product.getPrice()));
        return view;
    }
}
