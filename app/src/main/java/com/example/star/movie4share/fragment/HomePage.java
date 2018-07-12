package com.example.star.movie4share.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.ProductDetailActivity;
import com.example.star.movie4share.dao.OrderDao;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.entity.Order;
import com.example.star.movie4share.entity.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *  用户的首页，含搜索和广告功能
 *  fragment[0]
 */
public class HomePage extends Fragment {

    private OnFragmentInteractionListener mListener;
    ProductDao dao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Product> allList = new ArrayList<>();
    private int allSize;

    private SearchView mSearchView;
    private ListView mListView;
    private SearchAdapter mAdapter;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onStart(){
        initSearch();

        mListView = (ListView) getActivity().findViewById(R.id.search_listview);

        mSearchView = (SearchView) getActivity().findViewById(R.id.search_bar);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setFocusable(true);
        mSearchView.clearFocus();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    final String str = s;
                    new Thread(){
                        @Override
                        public void run(){
                            Message msg = Message.obtain();
                            msg.what = 1234;
                            Bundle bundle = new Bundle();
                            bundle.putString("text", str);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                } else {
                    initSearch();
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                if (mSearchView != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                    }
                    mSearchView.clearFocus();
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        super.onStart();
    }

    private void initSearch(){
        new Thread(){
            @Override
            public void run(){
                Message msg = Message.obtain();
                msg.what = 8685;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * @param msg 收到消息
     * 8685：读取所有商品并init
     * 1234："text"对应传入的搜索关键词，读取数据库搜到商品并传入adapter
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            switch (what){
                case 8685:
                    allList = (ArrayList<Product>) dao.loadAll();
                    allSize = allList.size();
                    Log.i("cc", "Size:" + allSize);
                    productList = (ArrayList<Product>) allList;
                    mAdapter = new HomePage.SearchAdapter(getContext(), productList);
                    mListView.setAdapter(mAdapter);
                    break;
                case 1234:
                    productList.clear();
                    allList = (ArrayList<Product>) dao.loadAll();
                    allSize = allList.size();
                    String str = msg.getData().getString("text");
                    Log.i("cc", "input string:" + str);
                    for (int i = 0; i < allSize; i++){
                        String getStr = allList.get(i).getProductName();
                        if (getStr.contains(str)) {
                            Log.d("cc", allList.get(i).getProductName() + "contains!" + str);
                            productList.add(allList.get(i));
                        }
                    }
                    Log.d("cc", "productList size:" + productList.size());
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public class SearchAdapter extends BaseAdapter {
        class Holder {
            ImageView mImg;
            TextView mName, mPrice, mCategory, mDescription;
            LinearLayout mWhole;
        }

        private LayoutInflater mInflater;
        private ArrayList<Product> mProductList = new ArrayList<>();
        private Holder holder;
        private Context mContext;
        ProductDao productDao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();

        public SearchAdapter(Context context, ArrayList<Product> mProductList) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            this.mProductList = mProductList;
        }

        @Override
        public int getCount() {
            return mProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return mProductList.get(position);
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
                convertView = mInflater.inflate(R.layout.search_item, parent, false);

                holder.mImg = (ImageView) convertView.findViewById(R.id.search_item_imgview);
                holder.mName = (TextView) convertView.findViewById(R.id.search_item_name);
                holder.mPrice = (TextView) convertView.findViewById(R.id.search_item_price);
                holder.mCategory = (TextView) convertView.findViewById(R.id.search_item_category);
                holder.mDescription = (TextView) convertView.findViewById(R.id.search_item_description);
                holder.mWhole = (LinearLayout) convertView.findViewById(R.id.search_item);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            final Product mProduct = (Product) getItem(position);
            holder.mName.setText("" + mProduct.getProductName());
            holder.mPrice.setText("价格：￥" + mProduct.getPrice());
            switch (mProduct.getCategory()){
                case "action":
                    holder.mCategory.setText("类别：动作片");
                    break;
                case "love":
                    holder.mCategory.setText("类别：爱情片");
                    break;
                case "fun":
                    holder.mCategory.setText("类别：喜剧片");
                    break;
                default:
                    holder.mCategory.setText("类别：什么片？");
                    break;
            }
//            holder.mCategory.setText("类别：" + mProduct.getCategory());
            holder.mDescription.setText("" + mProduct.getShortDescription());
            Picasso.get().load(mProduct.getUrl()).resize(200,200).centerInside().into(holder.mImg);

            //点击商品进入详情页
            holder.mWhole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productId", mProduct.getId());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
