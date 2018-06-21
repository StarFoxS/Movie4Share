package com.example.star.movie4share.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.MainActivity;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.example.star.movie4share.fragment.ShopCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2018/6/20.
 */

public class ShopCartAdapter extends BaseAdapter{

    class Holder {
        CheckBox mCB;
        ImageView mImg;
        TextView mName, mCategory, mPrice, mNumber, mStock;
        Button mAddBtn, mMinusBtn;
    }

    private LayoutInflater mInflater;
    private List<ShopCartProduct> mShopCartItem = new ArrayList<>();
    private Holder holder;
    private Context mContext;
    ShopCartProductDao cartDao = Movie4ShareApplication.getInstances().getDaoSession().getShopCartProductDao();
//    private CheckBox mCB;
//    private ImageView mImg;
//    private TextView mName, mCategory, mPrice, mNumber, mStock;
//    private Button mAddBtn, mMinusBtn;

    public ShopCartAdapter(Context context, List<ShopCartProduct> mSCI) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mShopCartItem = mSCI;
    }

    @Override
    public int getCount() {
        return mShopCartItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopCartItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.shop_cart_item, parent, false);
            holder.mCB = (CheckBox) convertView.findViewById(R.id.shopcart_item_checkbox);
            holder.mName = (TextView) convertView.findViewById(R.id.shopcart_item_name);
            holder.mCategory = (TextView) convertView.findViewById(R.id.shopcart_item_category);
            holder.mPrice = (TextView) convertView.findViewById(R.id.shopcart_item_price);
            holder.mNumber = (TextView) convertView.findViewById(R.id.shopcart_item_number);
            holder.mImg = (ImageView) convertView.findViewById(R.id.shopcart_item_imageview);
            holder.mAddBtn = (Button) convertView.findViewById(R.id.shopcart_add);
            holder.mMinusBtn = (Button) convertView.findViewById(R.id.shopcart_minus);
            holder.mStock = (TextView) convertView.findViewById(R.id.shopcart_stock);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ShopCartProduct mProduct = mShopCartItem.get(position);
        holder.mCategory.setText(mProduct.getCategory());
        holder.mName.setText(mProduct.getName());
        holder.mPrice.setText(mProduct.getPrice() + "");
        holder.mNumber.setText(mProduct.getNumber() + "");
        holder.mStock.setText(mProduct.getStock() + "");
        Picasso.get().load(mProduct.getImgUrl()).into(holder.mImg);


        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long itemId = mProduct.getId();
                int num = mShopCartItem.get(position).getNumber();

                if (mProduct.getStock() >= num+1) {
                    ShopCartProduct nShopCartProduct = new ShopCartProduct(mShopCartItem.get(position).getId(),
                            mShopCartItem.get(position).getName(), mShopCartItem.get(position).getCategory(), mShopCartItem.get(position).getPrice(),
                            num+1, mShopCartItem.get(position).getImgUrl(), mShopCartItem.get(position).getStock());
                    cartDao.update(nShopCartProduct);
                    holder.mNumber.setText(num+1 + "");
                    ShopCart.updateTotal();
                    mShopCartItem.get(position).setNumber(num+1);
                } else {
                    Toast.makeText(mContext, "库存没有更多啦！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long itemId = mProduct.getId();
                        int num = mShopCartItem.get(position).getNumber();

                        if (mProduct.getNumber()-1 != 0) {
                            ShopCartProduct nShopCartProduct = new ShopCartProduct(mShopCartItem.get(position).getId(),
                                    mShopCartItem.get(position).getName(), mShopCartItem.get(position).getCategory(), mShopCartItem.get(position).getPrice(),
                                    num-1, mShopCartItem.get(position).getImgUrl(), mShopCartItem.get(position).getStock());
                            cartDao.update(nShopCartProduct);
                            holder.mNumber.setText(num-1 + "");
                            ShopCart.updateTotal();
                            mShopCartItem.get(position).setNumber(num-1);
                        } else {
                            Toast.makeText(mContext, "还是至少买一个吧！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        holder.mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long itemId = mProduct.getId();

                if (isChecked) {
                    if (!ShopCart.checkProduct.contains(itemId)) {
                        ShopCart.checkProduct.add(itemId);
                        ShopCart.orderItem.add(mShopCartItem.get(position));
                    }
                } else {
                    if (ShopCart.checkProduct.contains(itemId)) {
                        ShopCart.checkProduct.remove(ShopCart.checkProduct.indexOf(itemId));
                        ShopCart.orderItem.remove(ShopCart.orderItem.indexOf(mShopCartItem.get(position)));
                    }

                }
                ShopCart.updateTotal();
            }
        });

        if (ShopCart.checkProduct.contains(mProduct.getId())){
            holder.mCB.setChecked(true);
        }
        else{
            holder.mCB.setChecked(false);
        }

        return convertView;
    }
}
