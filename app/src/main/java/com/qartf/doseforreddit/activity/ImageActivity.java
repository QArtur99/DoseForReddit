package com.qartf.doseforreddit.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qartf.doseforreddit.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ImageActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.picture) ImageView picture;
    @BindView(R.id.layoutFrame) RelativeLayout layoutFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        setToolbar();
        layoutFrame.setOnClickListener(this);

        String link = getIntent().getStringExtra("link");

        Glide.with(this)
                .load(link)
                .thumbnail(Glide.with(this).load(R.drawable.ic_ondemand_video))
                .into(picture);



    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}
