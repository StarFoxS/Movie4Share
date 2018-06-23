package com.example.star.movie4share.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.adapter.ConfirmOrderAdapter;
import com.example.star.movie4share.dao.OrderDao;
import com.example.star.movie4share.entity.Order;
import com.example.star.movie4share.entity.Product;
import com.example.star.movie4share.entity.ShopCartProduct;
import com.example.star.movie4share.fragment.ShopCart;

import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView tvReceiverName;
    private EditText etReceiverName;
    private TextView tvReceiverAddress;
    private EditText etReceiverAddress;
    private TextView tvReceiverPhone;
    private EditText etReceiverPhone;

    private RelativeLayout editRelative;
    private RelativeLayout submitRelative;

    private TextView tvSubmit;
    private TextView tvCancel;

    private ListView productList;
    private TextView tvTotalNum;
    private TextView tvTotalPrice;
    private Button orderBtn;

    private String newReceiverName;
    private String newReceiverAddr;
    private String newReceiverPhone;

    private int productSize;
    private int productNum;
    private double totalPrice;

    OrderDao orderDao = Movie4ShareApplication.getInstances().getDaoSession().getOrderDao();
    private ConfirmOrderAdapter confirmOrderAdapter;

    private ArrayList<ShopCartProduct> mOrderProduct = new ArrayList<>();
    public ArrayList<Long> mOrderProductId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        tvReceiverName = (TextView) findViewById(R.id.activity_confirm_receiver_name_textview);
        tvReceiverAddress = (TextView) findViewById(R.id.activity_confirm_receiver_address_textview);
        tvReceiverPhone = (TextView) findViewById(R.id.activity_confirm_receiver_phone_textview);
        etReceiverName = (EditText) findViewById(R.id.activity_confirm_receiver_name_edittext);
        etReceiverAddress = (EditText) findViewById(R.id.activity_confirm_receiver_address_edittext);
        etReceiverPhone = (EditText) findViewById(R.id.activity_confirm_receiver_phone_edittext);

        editRelative = (RelativeLayout) findViewById(R.id.activity_confirm_edit_relative);
        submitRelative = (RelativeLayout) findViewById(R.id.activity_confirm_submit_relative);
        editListener();

        tvSubmit = (TextView) findViewById(R.id.activity_confirm_submit_save_textview);
        tvCancel = (TextView)findViewById(R.id.activity_confirm_submit_cancel_textview);
        submitListener();
        cancelListener();

        showDefaultReceiver();

        Intent intent = this.getIntent();
        productSize = (Integer)intent.getSerializableExtra("productSize");
        totalPrice = (Double) intent.getSerializableExtra("totalPrice");
        Log.i("cc", "size:" + productSize + " price:" + totalPrice);
        for (int i = 0; i < productSize; i++){
            mOrderProduct.add( (ShopCartProduct) intent.getSerializableExtra("OrderedProduct" + i));
            mOrderProductId.add( (Long) intent.getSerializableExtra("CheckedProductsId" + i));
        }

        insertOrder();

        productList = (ListView)findViewById(R.id.confirm_product_list);
        confirmOrderAdapter = new ConfirmOrderAdapter(this, mOrderProduct);
        productList.setAdapter(confirmOrderAdapter);

        tvTotalNum = (TextView)findViewById(R.id.activity_confirm_product_num);
        tvTotalPrice = (TextView)findViewById(R.id.activity_confirm_total_price);
        tvTotalNum.setText("共" + productSize + "种商品");
        tvTotalPrice.setText("总价：" + totalPrice);

        orderBtn = (Button)findViewById(R.id.activity_confirm_order_button);
        orderBtnListener();

    }

    private void editListener() {
        editRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRelative.setVisibility(View.VISIBLE);
                editRelative.setVisibility(View.GONE);
                tvReceiverName.setVisibility(View.GONE);
                tvReceiverAddress.setVisibility(View.GONE);
                tvReceiverPhone.setVisibility(View.GONE);
                etReceiverName.setVisibility(View.VISIBLE);
                etReceiverAddress.setVisibility(View.VISIBLE);
                etReceiverPhone.setVisibility(View.VISIBLE);
            }
        });
    }

    private void submitListener(){
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ConfirmOrderActivity.this, "信息已提交", Toast.LENGTH_SHORT).show();
//                Boolean flag = checkReceiver();
                String nName = etReceiverName.getText().toString();
                String nAddress = etReceiverAddress.getText().toString();
                String nPhone = etReceiverPhone.getText().toString();

                // 信息验证
                if (nName.length() >= 2 && (nPhone.length() == 11 || nPhone.length() == 5 )) {
                    newReceiverName = nName;
                    newReceiverAddr = nAddress;
                    newReceiverPhone = nPhone;

                    Toast.makeText(ConfirmOrderActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    //TODO: 把修改后的新信息上传为user下次购物的默认显示
                    submitRelative.setVisibility(View.GONE);
                    editRelative.setVisibility(View.VISIBLE);
                    tvReceiverName.setVisibility(View.VISIBLE);
                    tvReceiverAddress.setVisibility(View.VISIBLE);
                    tvReceiverPhone.setVisibility(View.VISIBLE);
                    etReceiverName.setVisibility(View.GONE);
                    etReceiverAddress.setVisibility(View.GONE);
                    etReceiverPhone.setVisibility(View.GONE);

                    tvReceiverName.setText(newReceiverName);
                    tvReceiverAddress.setText(newReceiverAddr);
                    tvReceiverPhone.setText(newReceiverPhone);
                } else if (nName.length() < 2) {
                    Toast.makeText(ConfirmOrderActivity.this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfirmOrderActivity.this, "请输入11位手机号或5位分机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancelListener(){
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRelative.setVisibility(View.GONE);
                editRelative.setVisibility(View.VISIBLE);
                tvReceiverName.setVisibility(View.VISIBLE);
                tvReceiverAddress.setVisibility(View.VISIBLE);
                tvReceiverPhone.setVisibility(View.VISIBLE);
                etReceiverName.setVisibility(View.GONE);
                etReceiverAddress.setVisibility(View.GONE);
                etReceiverPhone.setVisibility(View.GONE);
                etReceiverName.setText("");
                etReceiverAddress.setText("");
                etReceiverPhone.setText("");
            }
        });
    }

    private void showDefaultReceiver(){
        //TODO: 默认显示与user.customid相关的那个Receiver
    }

    private void insertOrder(){
        Time time = new Time();
        //long id, long userId, String time, String status, String serialNum, double price, int productNum, String imgUrl
        //TODO: 这个id应该是autoincrement的，回头研究下,img改一改
        Order nOrder = new Order(0, 1, time.year + "." + time.month+1 + "." + time.monthDay,
                "未确认", time.year + time.month + time.monthDay + time.second + time.minute + time.yearDay + "",
                totalPrice, productNum, "https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1468487353.74.webp");
        orderDao.insert(nOrder);
    }

    private void orderBtnListener() {
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: 从数据库中删除

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOrderActivity.this);
                builder.setTitle("密码验证");
                builder.setIcon(R.drawable.star);
                final EditText editText = new EditText(ConfirmOrderActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputPassword = editText.getText().toString();
                        if (inputPassword.equals("1")) { //default pw = 1; TODO: 应该等于user的密码
                            Toast.makeText(ConfirmOrderActivity.this, "验证成功", Toast.LENGTH_SHORT).show();

                            //TODO: 验证成功后的支付页面

                            final ProgressDialog progressDialog = new ProgressDialog(ConfirmOrderActivity.this,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("正在转到支付页面...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    payThread.start();
                                    progressDialog.dismiss();
                                }
                            },3000);

//                            payThread.start();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ConfirmOrderActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ConfirmOrderActivity.this);
                        builder2.setTitle("").setMessage("支付未完成，是否继续付款？");
                        builder2.setPositiveButton("再次支付", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 此处为空
                            }
                        });
                        builder2.setNegativeButton("下次再付", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO: 去我的订单
                            }
                        });
                        builder2.setCancelable(false);
                        AlertDialog dialog2 = builder2.create();
                        dialog2.setCanceledOnTouchOutside(true);
                        dialog2.show();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });
    }

    private Thread payThread = new Thread() {
        @Override
        public void run() {
            super.run();

            Message msg = new Message();
            msg.what = 016;
            payHandler.sendMessage(msg);
        }
    };

    private Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.what == 016){
                //TODO: 假装支付的API接口，支付方式：招商银行信用卡/让同事请客/找老板报销
                String dialogShow = "支付成功";
                new AlertDialog.Builder(ConfirmOrderActivity.this).setTitle("")
                        .setMessage(dialogShow).setPositiveButton("去看看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(){
                            @Override
                            public void run(){
                                super.run();
                                //TODO: 去我的订单页
                            }
                        }.start();
                    }
                }).setCancelable(false).show();
            }
        }
    };
}
