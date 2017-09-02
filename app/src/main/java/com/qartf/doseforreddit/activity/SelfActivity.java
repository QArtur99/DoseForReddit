package com.qartf.doseforreddit.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelfActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.selftext) TextView textView;
    @BindView(R.id.layoutFrame) RelativeLayout layoutFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        ButterKnife.bind(this);
        setToolbar();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        layoutFrame.setOnClickListener(this);


        String selfTextString = getIntent().getStringExtra("link");
        textView.setText(selfTextString);





    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
