package com.example.star.movie4share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.example.star.movie4share.fragment.ShopCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2018/6/22.
 */

public class ConfirmOrderAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<ShopCartProduct> mSCI = new ArrayList<>();
    private Holder holder;
    private Context mContext;

    class Holder {
        ImageView mImg;
        TextView mName, mPrice, mNumber;
    }


    public ConfirmOrderAdapter(Context context, ArrayList<ShopCartProduct> mSCI) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.mSCI = mSCI;
    }

    @Override
    public int getCount() {
        return mSCI.size();
    }

    @Override
    public Object getItem(int position) {
        return mSCI.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.confirm_order_item, parent, false);
            holder.mName = (TextView) convertView.findViewById(R.id.confirm_order_item_name_textview);
            holder.mPrice = (TextView) convertView.findViewById(R.id.confirm_order_item_price_textview);
            holder.mNumber = (TextView) convertView.findViewById(R.id.confirm_order_item_num_textview);
            holder.mImg = (ImageView) convertView.findViewById(R.id.confirm_order_item_imgview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ShopCartProduct mProduct = (ShopCartProduct) getItem(position);
        holder.mName.setText(mProduct.getName());
        holder.mPrice.setText(mProduct.getPrice() + "");
        holder.mNumber.setText("x" + mProduct.getNumber());
        Picasso.get().load(mProduct.getImgUrl()).into(holder.mImg);

        return convertView;
    }
}
