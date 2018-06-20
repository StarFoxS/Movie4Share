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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.movie4share.R;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.example.star.movie4share.fragment.ShopCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Star on 2018/6/20.
 */

public class ShopCartAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ShopCartProduct> mData = new ArrayList<>();
//    private ViewHolder holder;
    private CheckBox mCB;
    private ImageView mImg;
    private TextView mName, mCategory, mPrice, mNumber, mStock;
    private Button mAddBtn, mMinusBtn;

    public ShopCartAdapter(Context context, ArrayList<ShopCartProduct> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        holder = null;
        if (convertView == null) {
//            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.shop_cart_item, parent, false);
            mCB = (CheckBox) convertView.findViewById(R.id.shopcart_item_checkbox);
            mName = (TextView) convertView.findViewById(R.id.shopcart_item_name);
            mCategory = (TextView) convertView.findViewById(R.id
                    .shopcart_item_category);
            mPrice = (TextView) convertView.findViewById(R.id
                    .shopcart_item_price_textview);
            mNumber = (TextView) convertView.findViewById(R.id
                    .shopcart_item_number);
            mImg = (ImageView) convertView.findViewById(R.id
                    .shopcart_item_imageview);
            mAddBtn = (Button) convertView.findViewById(R.id.shopcart_add);
            mMinusBtn = (Button) convertView.findViewById(R.id.shopcart_minus);
            mStock = (TextView) convertView.findViewById(R.id.shopcart_stock);

//            convertView.setTag(holder);
        } else {
//            holder = (ViewHolder) convertView.getTag();
        }

        final ShopCartProduct mProduct = (ShopCartProduct) getItem(position);
        mCategory.setText(mProduct.getCategory());
        mName.setText(mProduct.getName());
        mPrice.setText(mProduct.getPrice() + "");
        mNumber.setText(mProduct.getNumber() + "");
        mStock.setText(mProduct.getStock() + "");
        Picasso.get().load(mProduct.getImgUrl()).into(mImg);

        //============================================================
//        holder.add_amount.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new Thread() {
//                            @Override
//                            public void run(){
//                                ProductReadDbHelper mDbHelper = new ProductReadDbHelper(getApplicationContext());
//                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
//                                ContentValues cv = new ContentValues();
//                                int amount = entity.getNumber();
//                                if(amount < entity.getStock()) {
//                                    amount++;
//
//                                    cv.put(ProductReaderContract.ProductEntry.COLUMN_NAME_NUMBER, amount);
//                                    db.update(ProductReaderContract.ProductEntry.TABLE_NAME, cv,
//                                            ProductReaderContract.ProductEntry.COLUMN_NAME_ENTRY_ID + "=?",
//                                            new String[]{entity.getId()});
//                                    Message msg = new Message();
//                                    msg.what = MSG_NUM;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("id", entity.getId());
//                                    bundle.putInt("op", 1);
//                                    msg.setData(bundle);
//                                    mHandler.sendMessage(msg);
//                                    totalThread.run();
//                                }
//                            }
//                        }.start();
//                    }
//                }
//        );
//
//        holder.reduce_amount.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new Thread() {
//                            @Override
//                            public void run(){
//                                ProductReadDbHelper mDbHelper = new ProductReadDbHelper(getApplicationContext());
//                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
//                                ContentValues cv = new ContentValues();
//                                int amount = entity.getNumber();
//                                Log.i("Amount", amount + "");
//                                if (amount > 1) {
//                                    amount--;
//
//                                    Log.i("Amount", amount + "");
//                                    cv.put(ProductReaderContract.ProductEntry.COLUMN_NAME_NUMBER, amount);
//                                    db.update(ProductReaderContract.ProductEntry.TABLE_NAME, cv,
//                                            ProductReaderContract.ProductEntry.COLUMN_NAME_ENTRY_ID + "=?",
//                                            new String[]{entity.getId()});
//                                    Message msg = new Message();
//                                    msg.what = MSG_NUM;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("id", entity.getId());
//                                    bundle.putInt("op", 2);
//                                    msg.setData(bundle);
//                                    mHandler.sendMessage(msg);
//                                    totalThread.run();
//                                }
//                            }
//                        }.start();
//                    }
//                }
//        );
        //============================================================


//        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int itemid = Integer.parseInt(entity.getId());
//
//                if (isChecked) {
//                    if (!CheckedProductId.contains(itemid)) {
//                        CheckedProductId.add(itemid);
//                        orderedDatas.add(mDatas.get(position));
//                        //mTotalMoney += entity.getNumber() * entity.getPrice();
//                        //mTotalChecked++;
//                        Log.i("Checked3:", CheckedProductId.toString());
//                    }
//                } else {
//                    if (CheckedProductId.contains(itemid)) {
//                        //mTotalMoney -= entity.getNumber() * entity.getPrice();
//                        //mTotalChecked--;
//                        CheckedProductId.remove(CheckedProductId.indexOf(itemid));
//                        orderedDatas.remove(orderedDatas.indexOf(mDatas.get(position)));
//                        Log.i("Checked4:", CheckedProductId.toString());
//                    }
//
//                }
//                //mBtnChecking.setText("去结算(" + mTotalChecked + ")");
//                //mTVTotal.setText("合计：" + mTotalMoney + " 爱心币");
//                totalThread.run();
//            }
//        });

        if (ShopCart.checkProduct.contains(mProduct.getId())){
            mCB.setChecked(true);
        }
        else{
            mCB.setChecked(false);
        }

        return convertView;
    }

//    class ViewHolder {
//        CheckBox cb;
//        ImageView img;
//        TextView name, category, price, number, stock;
//        Button add_amount, reduce_amount;
//    }
}
