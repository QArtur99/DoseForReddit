package com.qartf.doseforreddit.ui.contentView;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.ui.BaseActivityChild;

import butterknife.BindView;



public class ImageActivity extends BaseActivityChild implements View.OnClickListener{

    @BindView(R.id.picture) ImageView picture;
    @BindView(R.id.layoutFrame) RelativeLayout layoutFrame;

    @Override
    public int getContentLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void initComponents() {
        layoutFrame.setOnClickListener(this);
        String link = getIntent().getStringExtra("link");
        Glide.with(this)
                .load(link)
                .thumbnail(Glide.with(this).load(R.drawable.ic_ondemand_video))
                .into(picture);
    }


    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}
