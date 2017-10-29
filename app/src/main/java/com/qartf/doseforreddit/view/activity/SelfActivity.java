package com.qartf.doseforreddit.view.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;

import butterknife.BindView;

public class SelfActivity extends BaseActivityChild implements View.OnClickListener{

    @BindView(R.id.selftext) TextView textView;
    @BindView(R.id.layoutFrame) RelativeLayout layoutFrame;


    @Override
    public int getContentLayout() {
        return R.layout.activity_self;
    }

    @Override
    public void initComponents() {
        layoutFrame.setOnClickListener(this);
        String selfTextString = getIntent().getStringExtra("link");
        textView.setText(selfTextString);
    }


    @Override
    public void onClick(View view) {
        onBackPressed();
    }

}
