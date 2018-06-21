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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.ComfirmOrder;
import com.example.star.movie4share.activity.MainActivity;
import com.example.star.movie4share.activity.ProductDetailActivity;
import com.example.star.movie4share.adapter.ProductAdapter;
import com.example.star.movie4share.adapter.ShopCartAdapter;
import com.example.star.movie4share.dao.DaoMaster;
import com.example.star.movie4share.dao.DaoSession;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;

import java.util.ArrayList;
import java.util.List;

public class ShopCart extends Fragment {

    private OnFragmentInteractionListener mListener;
    static ShopCartProductDao cartDao = Movie4ShareApplication.getInstances().getDaoSession().getShopCartProductDao();

    private ListView mListView;

    public static ArrayList<Long> checkProduct = new ArrayList<>();
    public static ArrayList<ShopCartProduct> orderItem = new ArrayList<>();

    private Button DelCheckedBtn;
    private Button DelAllBtn;
    private static Button GotoPayBtn;

    private static TextView mTextView;
    private CheckBox mCheckBox;

    private static double mTotalPrice = 0.0;
    private static int mCheckNum = 0;

    private List<ShopCartProduct> mShopCartItem = new ArrayList<>();
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

        mShopCartItem = cartDao.loadAll();

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
        mAdapter = new ShopCartAdapter(getContext(), mShopCartItem);
        mListView.setAdapter(mAdapter);
    }

    // 获取Item数量与价格并计算总价
    public static void updateTotal() {
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
}
