package com.example.star.movie4share.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.dao.UserDao;
import com.example.star.movie4share.entity.User;

public class Profile extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPhone;
    private Button mBtn;

    private User user;
    private UserDao userDao;

    public Profile() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart(){

        mEtName = (EditText) getActivity().findViewById(R.id.profile_name_edittext);
        mEtEmail = (EditText) getActivity().findViewById(R.id.profile_email_edittext);
        mEtPhone = (EditText) getActivity().findViewById(R.id.profile_phone_edittext);
        mBtn = (Button) getActivity().findViewById(R.id.profile_button);

        initProfile();
        btnListener();

        super.onStart();
    }

    private void initProfile(){
        new Thread(){
            @Override
            public void run(){
                Message msg = Message.obtain();
                msg.what = 987;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void btnListener(){
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        Message msg = Message.obtain();
                        msg.what = 789;
                        mHandler.sendMessage(msg);
                    }
                }.start();
                Toast.makeText(getContext(), "修改成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            switch (what){
                case 987:
                    userDao = Movie4ShareApplication.getInstances().getDaoSession().getUserDao();
                    user = userDao.load(Movie4ShareApplication.userId);
                    if (!user.getName().equals("")){
                        mEtName.setText(user.getName());
                    }
                    if (!user.getEmail().equals("")){
                        mEtEmail.setText(user.getEmail());
                    }
                    if (!user.getPhoneNum().equals("")){
                        mEtPhone.setText(user.getPhoneNum());
                    }
                    break;
                case 789:
                    User nUser = new User(user.getId(), user.getCustomId(), user.getPassword(), user.getShowpw(),
                            mEtEmail.getText().toString(), mEtName.getText().toString(), mEtPhone.getText().toString(),
                            user.getImgUrl());
                    userDao.insertOrReplace(nUser);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
