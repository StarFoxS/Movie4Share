package com.example.star.movie4share.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.AdminActivity;
import com.example.star.movie4share.entity.User;
import com.example.star.movie4share.fragment.ViewOrder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2018/6/25.
 */

public class AdminAdapter extends BaseAdapter {

    class Holder {
        ImageView mImg;
        TextView mUser, mPw1, mPw2, mEmail, mName, mPhone;
        LinearLayout mWhole;
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
            convertView.setTag(holder);
        } else {
            holder = (AdminAdapter.Holder) convertView.getTag();
        }

        final User mUser = (User) getItem(position);
        holder.mUser.setText(mUser.getCustomId());
        holder.mPw2.setText(mUser.getPassword());
        holder.mEmail.setText(mUser.getEmail());
        holder.mName.setText(mUser.getName());
        holder.mPhone.setText(mUser.getPhoneNum());
        Picasso.get().load(mUser.getImgUrl()).into(holder.mImg);

        holder.mWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Message msg = Message.obtain();
//                msg.what = 1111;
//                mHandler.sendMessage(msg);

                holder.mPw1.setVisibility(View.VISIBLE);
                holder.mPw1.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.mPw1.setVisibility(View.GONE);
                        holder.mPw2.setVisibility(View.GONE);
                    }
                },3000);
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
                        String inputUserName = editTextUser.getText().toString();
                        String inputPassword = editTextPw.getText().toString();
                        if (inputUserName.contains("@")) {
                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                            //TODO: 把user信息改掉
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

        return convertView;
    }
}
