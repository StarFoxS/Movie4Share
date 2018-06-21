package com.example.star.movie4share.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.ComfirmOrder;
import com.example.star.movie4share.activity.MainActivity;
import com.example.star.movie4share.activity.ProductDetailActivity;
import com.example.star.movie4share.adapter.ProductAdapter;
import com.example.star.movie4share.dao.DaoMaster;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopCart extends Fragment {

    private OnFragmentInteractionListener mListener;
    static ShopCartProductDao cartDao = Movie4ShareApplication.getInstances().getDaoSession().getShopCartProductDao();

    private ListView mListView;
    public ShopCart shopCart = null;

    public ArrayList<Long> checkProduct = new ArrayList<>();
    public ArrayList<ShopCartProduct> orderItem = new ArrayList<>();

    private Button DelCheckedBtn;
    private Button DelAllBtn;
    private Button GotoPayBtn;

    private TextView mTextView;
    private CheckBox mCheckBox;

    private double mTotalPrice = 0.0;
    private int mCheckNum = 0;

    private ArrayList<ShopCartProduct> mShopCartItem = new ArrayList<>();
    private ShopCartAdapter mAdapter;


    public ShopCart() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_cart, container, false);
        return view;
    }

    @Override
    public void onStart(){

        mListView = (ListView) getActivity().findViewById(R.id.shopcart_listview);
        GotoPayBtn = (Button) getActivity().findViewById(R.id.fragment_shopcart_btn_goto_payment);
        DelCheckedBtn = (Button) getActivity().findViewById(R.id.fragment_shopcart_btn_delete_checked_product);
        DelAllBtn = (Button) getActivity().findViewById(R.id.fragment_shopcart_btn_delete_all_product);
        mTextView = (TextView) getActivity().findViewById(R.id.fragment_shopcart_total_price);
        mCheckBox = (CheckBox) getActivity().findViewById(R.id.shopcart_item_checkbox_all);

        gotoPayListener();
        deleteCheckedListener();
        deleteAllListener();

        mShopCartItem = (ArrayList<ShopCartProduct>) cartDao.loadAll();

        CheckBoxListener();
        initShopCart();

        super.onStart();
    }

    private Thread totalThread = new Thread() {
        @Override
        public void run() {
            super.run();
            updateTotal();
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 111:
                    mAdapter = new ShopCartAdapter(getContext(), mShopCartItem);
                    mListView.setAdapter(mAdapter);
                    CheckBoxListener();
                    break;
                case 222:
                    for(int i = 0; i < mShopCartItem.size(); i++){
                        ShopCartProduct shopCartProduct = mShopCartItem.get(i);
                        if(shopCartProduct.getId() == msg.getData().getLong("id")) {
                            if (msg.getData().getString("operation") == "plus")
                                shopCartProduct.setNumber(shopCartProduct.getNumber() + 1);
                            else if (msg.getData().getString("operation") == "minus")
                                shopCartProduct.setNumber(shopCartProduct.getNumber() - 1);
                            mShopCartItem.set(i, shopCartProduct);
                            break;
                        }
                    }
//                    try {
                        mAdapter.notifyDataSetChanged();
//                    } catch (Exception e){
//                        Log.d("cc","notifyDataSetChanged not done");
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    /*
     * 结算按钮
     */
    private void gotoPayListener() {
        GotoPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Movie4ShareApplication.loginStatus == "user" ) {
                    if (orderItem.size() > 0){
                        Intent intent = new Intent(getActivity().getApplicationContext(), ComfirmOrder.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("productSize", Integer.valueOf(orderItem.size()));
                        bundle.putSerializable("totalPrice", mTotalPrice);
                        for (int i = 0; i < orderItem.size(); ++i) {
                            bundle.putSerializable("OrderedProduct" + i, orderItem.get(i));
                            bundle.putSerializable("CheckedProductsId" + i, checkProduct.get(i));
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(),"请选择商品",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"您所在的用户组无法购买商品-_-b",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
     * 清除已选商品
     */
    private void deleteCheckedListener(){
        DelCheckedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 找到那些被选中的，在dao里删除，然后在界面上把这些删除
            }
        });
    }

    /*
     * 删除所有商品
     */
    private void deleteAllListener() {
        DelAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartDao.deleteAll();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("casefragment","refreshshopcart");
                startActivity(intent);
            }
        });
    }

    /*
     * 全选按钮监听与操作
     */
    private void CheckBoxListener(){
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkProduct.clear();
                orderItem.clear();
                if (isChecked) {
                    mTotalPrice = 0;
                    mCheckNum = 0;
                    for (int i = 0; i < mShopCartItem.size(); ++i) {
                        checkProduct.add(mShopCartItem.get(i).getId());
                        orderItem.add(mShopCartItem.get(i));
                    }
                }
//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initShopCart(){
        new Thread(){
            @Override
            public void run(){
                Message message = Message.obtain();
                message.what = 111;
                mHandler.sendMessage(message);
            }
        }.start();
        GotoPayBtn.setText("结算(0)");
        mTextView.setText("合计：￥0.0");
    }

    // 获取Item数量与价格并计算总价
    private void updateTotal() {
        double totalprice = 0;
        int totalnum = 0;
        for (int i = 0; i < checkProduct.size(); ++i){
//            long checkId = checkProduct.get(i);
            ShopCartProduct nShopCartProduct = cartDao.load(checkProduct.get(i));
            if (nShopCartProduct != null) {
                totalprice += nShopCartProduct.getNumber() * nShopCartProduct.getPrice();
                totalnum += nShopCartProduct.getNumber();
            }
        }
//        mCheckNum = checkProduct.size();
        mTotalPrice = totalprice;
        mCheckNum = totalnum;
        GotoPayBtn.setText("结算(" + mCheckNum + ")");
        mTextView.setText("合计：￥" + mTotalPrice);
        Log.i("cc Total Price", "mTotalPrice:" + mTotalPrice + " mCheckNum:" + mCheckNum);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /*
     * ShopCartAdapter Class
     * 因为要用到ShopCart Fragment里的变量，但已经继承了BaseAdapter，不知道怎么再继承ShopCart
     * 于是强行放在ShopCart Fragment里了
     * 之前的错误是position这个参数永远只读取到最后一个进入的position，改动时前面产生的不能改
     */

    public class ShopCartAdapter extends BaseAdapter {

        class Holder {
            CheckBox mCB;
            ImageView mImg;
            TextView mName, mCategory, mPrice, mNumber, mStock;
            Button mAddBtn, mMinusBtn;
        }

        private LayoutInflater mInflater;
        // mSCI = mShopCartItem
        private ArrayList<ShopCartProduct> mSCI = new ArrayList<>();
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
            this.mSCI = mSCI;
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

            final ShopCartProduct mProductAdapter = (ShopCartProduct) getItem(position);
            holder.mCategory.setText(mProductAdapter.getCategory());
            holder.mName.setText(mProductAdapter.getName());
            holder.mPrice.setText(mProductAdapter.getPrice() + "");
            holder.mNumber.setText(mProductAdapter.getNumber() + "");
            holder.mStock.setText(mProductAdapter.getStock() + "");
            Picasso.get().load(mProductAdapter.getImgUrl()).into(holder.mImg);


            holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mProductAdapter.getNumber() >= mProductAdapter.getStock()){
                        Toast.makeText(getContext(), "库存没有更多啦！", Toast.LENGTH_SHORT).show();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            int num = mProductAdapter.getNumber();

                            if (mProductAdapter.getStock() > num) {

                                num++;

                                ShopCartProduct nShopCartProduct = new ShopCartProduct(mProductAdapter.getId(),
                                        mProductAdapter.getName(), mProductAdapter.getCategory(), mProductAdapter.getPrice(),
                                        num, mProductAdapter.getImgUrl(), mProductAdapter.getStock());
                                cartDao.update(nShopCartProduct);

                                Message message = Message.obtain();
                                message.what = 222;
                                Bundle bundle = new Bundle();
                                bundle.putLong("id", mProductAdapter.getId());
                                bundle.putString("operation", "plus");
                                message.setData(bundle);
                                mHandler.sendMessage(message);

                                totalThread.run();
                            }
//                            else {
//                                Toast.makeText(getContext(), "库存没有更多啦！", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }.start();
                }
            });

            holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mProductAdapter.getNumber() <= 1){
                        Toast.makeText(getContext(), "至少也要买一个吧！", Toast.LENGTH_SHORT).show();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            int num = mProductAdapter.getNumber();

                            if (num > 1) {

                                num--;

                                ShopCartProduct nShopCartProduct = new ShopCartProduct(mProductAdapter.getId(),
                                        mProductAdapter.getName(), mProductAdapter.getCategory(), mProductAdapter.getPrice(),
                                        num, mProductAdapter.getImgUrl(), mProductAdapter.getStock());
                                cartDao.update(nShopCartProduct);

                                Message message = Message.obtain();
                                message.what = 222;
                                Bundle bundle = new Bundle();
                                bundle.putLong("id", mProductAdapter.getId());
                                bundle.putString("operation", "minus");
                                message.setData(bundle);
                                mHandler.sendMessage(message);

                                totalThread.run();
                            }
//                            else {
//                                Toast.makeText(getContext(), "至少也要买一个吧！", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }.start();
                }
            });


            holder.mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long itemId = mProductAdapter.getId();

                    if (isChecked) {
                        if (!checkProduct.contains(itemId)) {
                            checkProduct.add(itemId);
                            orderItem.add(mSCI.get(position));
                        }
                    } else {
                        if (checkProduct.contains(itemId)) {
                            checkProduct.remove(checkProduct.indexOf(itemId));
                            orderItem.remove(orderItem.indexOf(mSCI.get(position)));
                        }

                    }
                    totalThread.run();
                }
            });

            if (checkProduct.contains(mProductAdapter.getId())){
                holder.mCB.setChecked(true);
            } else{
                holder.mCB.setChecked(false);
            }

            return convertView;
        }
    }
}
