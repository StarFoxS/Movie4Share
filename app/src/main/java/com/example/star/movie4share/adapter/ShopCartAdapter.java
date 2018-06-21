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

public class ShopCartAdapter extends BaseAdapter {

    class Holder {
        CheckBox mCB;
        ImageView mImg;
        TextView mName, mCategory, mPrice, mNumber, mStock;
        Button mAddBtn, mMinusBtn;
    }

    private LayoutInflater mInflater;
    private ArrayList<ShopCartProduct> mShopCartItem = new ArrayList<>();
    private Holder holder;
    private Context mContext;
    ShopCartProductDao cartDao = Movie4ShareApplication.getInstances().getDaoSession().getShopCartProductDao();
//    private CheckBox mCB;
//    private ImageView mImg;
//    private TextView mName, mCategory, mPrice, mNumber, mStock;
//    private Button mAddBtn, mMinusBtn;

    public ShopCartAdapter(Context context, ArrayList<ShopCartProduct> mSCI) {
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
        Log.d("cc", "Position:" + position);
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

        final ShopCartProduct mProduct = (ShopCartProduct) getItem(position);
        holder.mCategory.setText(mProduct.getCategory());
        holder.mName.setText(mProduct.getName());
        holder.mPrice.setText(mProduct.getPrice() + "");
        holder.mNumber.setText(mProduct.getNumber() + "");
        holder.mStock.setText(mProduct.getStock() + "");
        Picasso.get().load(mProduct.getImgUrl()).into(holder.mImg);


        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        int num = mProduct.getNumber();

                        if (mProduct.getStock() > num) {

                            num++;

                            ShopCartProduct nShopCartProduct = new ShopCartProduct(mShopCartItem.get(position).getId(),
                                    mShopCartItem.get(position).getName(), mShopCartItem.get(position).getCategory(), mShopCartItem.get(position).getPrice(),
                                    num, mShopCartItem.get(position).getImgUrl(), mShopCartItem.get(position).getStock());
                            cartDao.update(nShopCartProduct);

                            Message message = Message.obtain();
                            message.what = 222;
                            Bundle bundle = new Bundle();
                            bundle.putLong("id", mShopCartItem.get(position).getId());
                            bundle.putString("operation", "plus");
                            message.setData(bundle);
//                            ShopCart.shopCart.mHandler.sendMessage(message);

//                            ShopCart.shopCart.totalThread.run();
                        } else {
                            Toast.makeText(mContext, "库存没有更多啦！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }
        });

        holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        int num = mProduct.getNumber();

                        if (num > 1) {

                            num--;

                            ShopCartProduct nShopCartProduct = new ShopCartProduct(mShopCartItem.get(position).getId(),
                                    mShopCartItem.get(position).getName(), mShopCartItem.get(position).getCategory(), mShopCartItem.get(position).getPrice(),
                                    num, mShopCartItem.get(position).getImgUrl(), mShopCartItem.get(position).getStock());
                            cartDao.update(nShopCartProduct);

                            Message message = Message.obtain();
                            message.what = 222;
                            Bundle bundle = new Bundle();
                            bundle.putLong("id", mShopCartItem.get(position).getId());
                            bundle.putString("operation", "minus");
                            message.setData(bundle);
 //                           ShopCart.shopCart.mHandler.sendMessage(message);

  //                          ShopCart.shopCart.totalThread.run();
                        } else {
                            Toast.makeText(mContext, "至少也要买一个吧！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();

            }
        });


        holder.mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long itemId = mProduct.getId();
//
//                if (isChecked) {
//                    if (!checkProduct.contains(itemId)) {
//                        ShopCart.shopCart.checkProduct.add(itemId);
//                        ShopCart.shopCart.orderItem.add(mShopCartItem.get(position));
//                    }
//                } else {
//                    if (ShopCart.shopCart.checkProduct.contains(itemId)) {
//                        ShopCart.shopCart.checkProduct.remove(ShopCart.shopCart.checkProduct.indexOf(itemId));
//                        ShopCart.shopCart.orderItem.remove(ShopCart.shopCart.orderItem.indexOf(mShopCartItem.get(position)));
//                    }
//
//                }
//                ShopCart.shopCart.totalThread.run();
            }
        });
//
//        if (ShopCart.shopCart.checkProduct.contains(mProduct.getId())){
//            holder.mCB.setChecked(true);
//        }
//        else{
//            holder.mCB.setChecked(false);
//        }

        return convertView;
    }
}
