package com.example.star.movie4share.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.adapter.AdminAdapter;
import com.example.star.movie4share.dao.UserDao;
import com.example.star.movie4share.entity.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton addUserBtn;
//    private ImageButton delUserBtn;
    private ListView mListView;

    private AdminAdapter mAdapter;
    private ArrayList<User> mUser = new ArrayList<>();
    UserDao userDao = Movie4ShareApplication.getInstances().getDaoSession().getUserDao();

    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin);

        addUserBtn = (ImageButton) findViewById(R.id.admin_add_user_imageBtn);
//        delUserBtn = (ImageButton) findViewById(R.id.admin_delete_user_imageBtn);
        layoutInflater = LayoutInflater.from(this);
        addUserListener();
//        delUserListener();

        mListView = (ListView) findViewById(R.id.admin_listview);

        mUser = (ArrayList<User>) userDao.loadAll();

        mAdapter = new AdminAdapter(this, mUser);
        mListView.setAdapter(mAdapter);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.admin_floating_btn);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (drawer.isDrawerOpen(navigationView)){
//                    drawer.closeDrawer(navigationView);
//                } else {
//                    drawer.openDrawer(navigationView);
//                }
//            }
//        });
    }

    private void addUserListener(){
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View dialogView = layoutInflater.inflate(R.layout.admin_dialog, null);
                final EditText editTextUser = (EditText) dialogView.findViewById(R.id.admin_dialog_user_edittext);
                final EditText editTextPw = (EditText)dialogView.findViewById(R.id.admin_dialog_pw_edittext);
                final EditText editTextVip = (EditText) dialogView.findViewById(R.id.admin_dialog_vip_edittext);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("新建用户信息");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(dialogView);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String inputUserName = editTextUser.getText().toString();
                        final String inputPassword = editTextPw.getText().toString();
                        final String inputVip = editTextVip.getText().toString();
                        if (inputUserName.contains("@")) {
                            Toast.makeText(AdminActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            new Thread(){
                                @Override
                                public void run(){
                                    Message msg = Message.obtain();
                                    msg.what = 133;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", inputUserName);
                                    bundle.putString("password", inputPassword);
                                    bundle.putString("vip", inputVip);
                                    msg.setData(bundle);
                                    mHandler.sendMessage(msg);
                                }
                            }.start();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AdminActivity.this, "放弃添加", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }

//    private void delUserListener(){
//        delUserBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//                final View dialogView = layoutInflater.inflate(R.layout.admin_dialog_del, null);
//                final EditText editTextUser = (EditText) dialogView.findViewById(R.id.admin_dialog_user_edittext);
//                final EditText editTextAgain = (EditText)dialogView.findViewById(R.id.admin_dialog_again_edittext);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
//                builder.setTitle("删除用户信息");
//                builder.setIcon(android.R.drawable.ic_dialog_info);
//                builder.setView(dialogView);
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        final String inputUserName = editTextUser.getText().toString();
//                        final String inputAgain = editTextAgain.getText().toString();
//                        if (inputUserName.equals(inputAgain)) {
//                            new Thread(){
//                                @Override
//                                public void run(){
//                                    List<User> userTry = userDao.loadAll();
//                                    for (int i = 0; i < userTry.size(); i++){
//                                        if (userTry.get(i).getCustomId().equals(inputUserName)){
//
//                                            Message msg = Message.obtain();
//                                            msg.what = 339;
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("username", inputUserName);
//                                            msg.setData(bundle);
//                                            mHandler.sendMessage(msg);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }.start();
////                            if (!flag){
////                                Toast.makeText(AdminActivity.this, "用户查无此人", Toast.LENGTH_SHORT).show();
////                            } else {
////                                Toast.makeText(AdminActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
////                            }
//                        } else {
//                            Toast.makeText(AdminActivity.this, "两次输入的不一致哦！", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(AdminActivity.this, "放弃添加", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setCancelable(true);
//                AlertDialog dialog = builder.create();
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//            }
//        });
//    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            switch (what){
                case 1111:
                    for (int i = 0; i < mUser.size(); i++){
                        User user = mUser.get(i);
                        if (user.getId() == msg.getData().getLong("userid")){
                            user.setShowpw(1 - user.getShowpw());
                            mUser.set(i, user);
                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2222:
                    for (int i = 0; i < mUser.size(); i++){
                        User user = mUser.get(i);
                        if (user.getId() == msg.getData().getLong("userid")){
                            user.setCustomId(msg.getData().getString("username"));
                            user.setPassword(msg.getData().getString("password"));
                            mUser.set(i, user);
                            break;
                        }
                    }
                    final User cUser = new User(msg.getData().getLong("userid"), msg.getData().getString("username"),
                            msg.getData().getString("password"), 0, "", "", "", "", 1, -1, -1);
                    new Thread(){
                        @Override
                        public void run(){
                            userDao.insertOrReplace(cUser);
                        }
                    }.start();
                    mAdapter.notifyDataSetChanged();
                    refresh();
                    break;
                case 133:
//                    int defaultID = R.drawable.user_default;
                    final User nUser = new User(5, msg.getData().getString("username"),
                            msg.getData().getString("password"), 0, "", "", "", "",
                            (double) Double.parseDouble(msg.getData().getString("vip")), -1, -1);  //id should be autoincrement
                    new Thread(){
                        @Override
                        public void run(){
                            userDao.insert(nUser);
                        }
                    }.start();
                    mAdapter.notifyDataSetChanged();
                    refresh();
                    break;
                case 339:
                    final String delUser = msg.getData().getString("username");
                    boolean flag = false;
                    for (int i = 0; i < mUser.size(); i++){
                        if (delUser.equals(mUser.get(i).getCustomId())){
                            final int ii = i;
                            new Thread(){
                                @Override
                                public void run(){
                                    userDao.deleteByKey(mUser.get(ii).getId());
                                }
                            }.start();
                            Toast.makeText(AdminActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            flag = true;
                            break;
                        }
                    }
                    if (!flag){
                        Toast.makeText(AdminActivity.this, "用户查无此人", Toast.LENGTH_SHORT).show();
                    }
//                    mUser = (ArrayList<User>) userDao.loadAll();
                    //TODO:删除与user有关的订单信息
                    mAdapter.notifyDataSetChanged();
                    refresh();
                    break;
                default:
                    break;
            }
        }
    };

    public void refresh(){
        finish();
        Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    public class AdminAdapter extends BaseAdapter {

        class Holder {
            ImageView mImg;
            TextView mUser, mPw1, mPw2, mEmail, mName, mPhone;
            LinearLayout mWhole;
            ImageButton mBtn;
        }

        private LayoutInflater layoutInflater;
        private List<User> mUser = new ArrayList<>();
        private Holder holder;
        private Context mContext;

        public AdminAdapter (Context context, ArrayList<User> mUser){
            this.mContext = context;
            layoutInflater = LayoutInflater.from(context);
            this.mUser = mUser;
        }

        @Override
        public int getCount() {
            return mUser.size();
        }

        @Override
        public Object getItem(int position) {
            return mUser.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = layoutInflater.inflate(R.layout.admin_item, parent, false);
                holder.mUser = (TextView) convertView.findViewById(R.id.admin_item_user_textview);
                holder.mPw1 = (TextView) convertView.findViewById(R.id.admin_item_pw);
                holder.mPw2 = (TextView) convertView.findViewById(R.id.admin_item_pw_textview);
                holder.mPw1.setVisibility(View.GONE);
                holder.mPw2.setVisibility(View.GONE);
                holder.mEmail = (TextView) convertView.findViewById(R.id.admin_item_email_textview);
                holder.mName = (TextView) convertView.findViewById(R.id.admin_item_name_textview);
                holder.mPhone = (TextView) convertView.findViewById(R.id.admin_item_phone_textview);
                holder.mImg = (ImageView) convertView.findViewById(R.id.admin_item_imgview);
                holder.mWhole = (LinearLayout) convertView.findViewById(R.id.admin_item);
                holder.mBtn = (ImageButton) convertView.findViewById(R.id.admin_delete_user_imageBtn);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            final User mUser = (User) getItem(position);
            holder.mUser.setText(mUser.getCustomId());
            holder.mPw2.setText(mUser.getPassword());
            if (mUser.getShowpw() == 1){
                holder.mPw1.setVisibility(View.VISIBLE);
                holder.mPw2.setVisibility(View.VISIBLE);
            } else {
                holder.mPw1.setVisibility(View.GONE);
                holder.mPw2.setVisibility(View.GONE);
            }
            holder.mEmail.setText(mUser.getEmail());
            holder.mName.setText(mUser.getName());
            holder.mPhone.setText(mUser.getPhoneNum());
            if (mUser.getImgUrl().equals("")){
                Picasso.get().load(R.drawable.user_default).into(holder.mImg);
            } else {
                Picasso.get().load(mUser.getImgUrl()).into(holder.mImg);
            }

            holder.mWhole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(){
                        @Override
                        public void run(){
                            Message msg = Message.obtain();
                            msg.what = 1111;
                            Bundle bundle = new Bundle();
                            bundle.putLong("userid", mUser.getId());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    }.start();


//                    holder.mPw1.setVisibility(View.VISIBLE);
//                    holder.mPw1.setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.mPw1.setVisibility(View.GONE);
//                            holder.mPw2.setVisibility(View.GONE);
//                        }
//                    },3000);
                }
            });

            holder.mWhole.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final View dialogView = layoutInflater.inflate(R.layout.admin_dialog, null);
                    final EditText editTextUser = (EditText) dialogView.findViewById(R.id.admin_dialog_user_edittext);
                    final EditText editTextPw = (EditText)dialogView.findViewById(R.id.admin_dialog_pw_edittext);

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("修改用户信息");
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setView(dialogView);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String inputUserName = editTextUser.getText().toString();
                            final String inputPassword = editTextPw.getText().toString();
                            if (inputUserName.contains("@")) {
                                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                //TODO: 把user信息改掉
                                new Thread(){
                                    @Override
                                    public void run(){
                                        Message msg = Message.obtain();
                                        msg.what = 2222;
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("userid", mUser.getId());
                                        bundle.putString("username", inputUserName);
                                        bundle.putString("password", inputPassword);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);
                                    }
                                }.start();
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(mContext, "放弃修改", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return true;
                }
            });

            holder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View dialogView = layoutInflater.inflate(R.layout.admin_dialog_del, null);
                    final EditText editTextUser = (EditText) dialogView.findViewById(R.id.admin_dialog_user_edittext);
                    final EditText editTextAgain = (EditText)dialogView.findViewById(R.id.admin_dialog_again_edittext);

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    builder.setTitle("确认删除用户信息");
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setView(dialogView);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String inputUserName = editTextUser.getText().toString();
                            final String inputAgain = editTextAgain.getText().toString();
                            if (inputUserName.equals(inputAgain)) {
                                new Thread(){
                                    @Override
                                    public void run(){
                                        List<User> userTry = userDao.loadAll();
                                        for (int i = 0; i < userTry.size(); i++){
                                            if (userTry.get(i).getCustomId().equals(inputUserName)){

                                                Message msg = Message.obtain();
                                                msg.what = 339;
                                                Bundle bundle = new Bundle();
                                                bundle.putString("username", inputUserName);
                                                msg.setData(bundle);
                                                mHandler.sendMessage(msg);
                                                break;
                                            }
                                        }
                                    }
                                }.start();
//                            if (!flag){
//                                Toast.makeText(AdminActivity.this, "用户查无此人", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(AdminActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//                            }
                            } else {
                                Toast.makeText(AdminActivity.this, "两次输入的不一致哦！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AdminActivity.this, "放弃删除", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logoff) {
            Movie4ShareApplication.loginStatus = "";
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
