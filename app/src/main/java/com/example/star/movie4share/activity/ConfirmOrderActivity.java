package com.example.star.movie4share.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.star.movie4share.R;

public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView tvReceiverName;
    private EditText etReceiverName;
    private TextView tvReceiverAddress;
    private EditText etReceiverAddress;
    private TextView tvReceiverPhone;
    private EditText etReceiverPhone;

    private RelativeLayout editRelative;
    private RelativeLayout submitRelative;

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
    }

    private void editListener() {
        editRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRelative.setVisibility(View.VISIBLE);

            }
        });
    }
}
