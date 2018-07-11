package com.example.star.movie4share.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.dao.OrderDao;
import com.example.star.movie4share.dao.OrderProductDao;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.Order;
import com.example.star.movie4share.entity.OrderProduct;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewOrder extends Fragment {

    private OnFragmentInteractionListener mListener;
    static OrderDao orderDao = Movie4ShareApplication.getInstances().getDaoSession().getOrderDao();

    private ListView orderList;
    private ViewOrderAdapter mAdapter;
    private ArrayList<Order> mOrderItem = new ArrayList<>();

    public ViewOrder() {
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
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        return view;
    }

    @Override
    public void onStart() {
        orderList = (ListView) getActivity().findViewById(R.id.fragment_view_order_list);

//        new Thread(){
//            @Override
//            public void run(){
//                mOrderItem = (ArrayList<Order>) orderDao.loadAll();
//            }
//        }.start();

        mOrderItem.clear();
        initOrder();

        super.onStart();
    }

    private void initOrder(){
        new Thread(){
            @Override
            public void run(){
                Message msg = Message.obtain();
                msg.what = 8685;
                mHandler.sendMessage(msg);
            }
        }.start();

    }

    private Thread mThread = new Thread() {
        @Override
        public void run(){
            super.run();
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            switch (what){
                case 8685:
                    ArrayList<Order> mAllItem;
                    mAllItem = (ArrayList<Order>) orderDao.loadAll();
                    mOrderItem.clear();
                    Log.i("cc", "this userid is:" + Movie4ShareApplication.userId);
                    for (int i = 0; i < mAllItem.size(); i++){
                        if (mAllItem.get(i).getUserId() == Movie4ShareApplication.userId){
                            mOrderItem.add(mAllItem.get(i));
                        }
                    }
                    Log.i("cc", "Size:" + mOrderItem.size());
                    mAdapter = new ViewOrderAdapter(getContext(), mOrderItem);
                    orderList.setAdapter(mAdapter);
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

    public class ViewOrderAdapter extends BaseAdapter{
        class Holder {
            ImageView mImg;
            TextView mPrice, mNum, mStatus, mSerial;
            Button mBtn;
        }

        private LayoutInflater mInflater;
        private ArrayList<Order> mOrderList = new ArrayList<>();
        private ViewOrder.ViewOrderAdapter.Holder holder;
        private Context mContext;
        OrderDao orderDao = Movie4ShareApplication.getInstances().getDaoSession().getOrderDao();

        public ViewOrderAdapter(Context context, ArrayList<Order> mOrderList) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            this.mOrderList = mOrderList;
        }

        @Override
        public int getCount() {
            return mOrderList.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrderList.get(position);
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
                convertView = mInflater.inflate(R.layout.view_order_item, parent, false);

                holder.mImg = (ImageView) convertView.findViewById(R.id.view_order_item_imgview);
                holder.mPrice = (TextView) convertView.findViewById(R.id.view_order_item_total_price_textview);
                holder.mNum = (TextView) convertView.findViewById(R.id.view_order_item_total_num_textview);
                holder.mStatus = (TextView) convertView.findViewById(R.id.view_order_item_status_textview);
                holder.mSerial = (TextView)convertView.findViewById(R.id.view_order_item_serial_num_textview);
                holder.mBtn = (Button) convertView.findViewById(R.id.view_order_item_button);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            final Order mOrder = (Order) getItem(position);
            holder.mPrice.setText("￥" + mOrder.getPrice());
            holder.mNum.setText(mOrder.getProductNum() + "");
            holder.mStatus.setText(mOrder.getStatus() + "");
            holder.mSerial.setText(mOrder.getSerialNum() + "");
            if (mOrder.getImgUrl().equals("")){
                Picasso.get().load(R.drawable.default_cargo).into(holder.mImg);
            } else {
                Picasso.get().load(mOrder.getImgUrl()).into(holder.mImg);
            }

            holder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return convertView;
        }
    }
}
