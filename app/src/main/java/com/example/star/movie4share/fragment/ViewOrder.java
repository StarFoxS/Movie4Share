package com.example.star.movie4share.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.ConfirmOrderActivity;
import com.example.star.movie4share.activity.MainActivity;
import com.example.star.movie4share.dao.OrderDao;
import com.example.star.movie4share.dao.OrderProductDao;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.dao.ShopCartProductDao;
import com.example.star.movie4share.entity.Order;
import com.example.star.movie4share.entity.OrderProduct;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
                case 666:
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
        ProductDao productDao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();

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

            if (mOrder.getStatus().equals("已发货")){
                holder.mBtn.setText("确认收货");
                holder.mBtn.setVisibility(View.VISIBLE);
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(){
                            @Override
                            public void run(){
                                mOrder.setStatus("已签收");
                                orderDao.insertOrReplace(mOrder);

                                Message message = Message.obtain();
                                message.what = 666;
                                Bundle bundle = new Bundle();
                                bundle.putLong("id", mOrder.getId());
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                            }
                        }.start();
                    }
                });
            } else {
                holder.mBtn.setVisibility(View.GONE);
            }

            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("订单详情");
                    builder.setIcon(R.drawable.default_cargo);

                    String s0 = "";
                    String s1 = "";
                    String s2 = "";
                    List<Product> allProduct = productDao.loadAll();
                    for (int i = 0; i < allProduct.size(); i++){
                        if (mOrder.getId0() == allProduct.get(i).getId()){
                            s0 = allProduct.get(i).getProductName();
                        }
                        if (mOrder.getId1() == allProduct.get(i).getId()){
                            s1 = allProduct.get(i).getProductName();
                        }
                        if (mOrder.getId2() == allProduct.get(i).getId()){
                            s2 = allProduct.get(i).getProductName();
                        }
                    }

                    String str;
                    if (mOrder.getId() == 50 || mOrder.getId() == 51){
                        str = "收货人:小招;  收货地址:来安路686号;  联系电话:38834600;  " + s0 + " x" + mOrder.getNum0();
                    } else {
                        str = "收货人:" + Movie4ShareApplication.orderName +
                                ";  收货地址:" + Movie4ShareApplication.orderAddr +
                                ";  联系电话:" + Movie4ShareApplication.orderPhone +
                                ";  " + s0 + " x" + mOrder.getNum0();
                    }
                    if (mOrder.getId1() != -1){
                        str += "  &  " + s1 + " x" + mOrder.getNum1();
                    }
                    if (mOrder.getId2() != -1){
                        str += "  &  " + s2 + " x" + mOrder.getNum2();
                    }

                    final TextView textView = new TextView(getContext());
                    textView.setText(str);
                    textView.setTextSize(18);
                    builder.setView(textView);
                    builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            });


            return convertView;
        }
    }
}
