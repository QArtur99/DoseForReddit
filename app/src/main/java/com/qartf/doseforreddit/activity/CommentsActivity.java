package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.network.RetrofitControl;

import butterknife.BindView;

public class CommentsActivity extends BaseViewActivity implements DetailFragment.DetailFragmentInterface{

    @BindView(R.id.toolbar) Toolbar toolbar;
    private Post post;

    CommentsActivity(){}

    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish();
            return;
        }

        setToolbar();
        retrofitControl = new RetrofitControl(CommentsActivity.this, this);
        Intent intent = getIntent();
        post = new Gson().fromJson(intent.getStringExtra("link"), new TypeToken<Post>() {}.getType());
        accessToken = new Gson().fromJson(intent.getStringExtra("token"), AccessToken.class);

        FragmentManager fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        detailFragment.setPost(post);
        fragmentManager.beginTransaction()
                .add(R.id.detailsViewFrame, detailFragment)
                .commit();
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
    public RetrofitControl getRetrofitControl() {
        return retrofitControl;
    }
}
