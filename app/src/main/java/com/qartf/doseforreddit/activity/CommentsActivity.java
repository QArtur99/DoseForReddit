package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.network.RetrofitControl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements RetrofitControl.RetrofitControlInterface,
        DetailFragment.DetailFragmentInterface{

    @BindView(R.id.toolbar) Toolbar toolbar;

    private Post post;
    private AccessToken accessToken;
    private DetailFragment detailFragment;
    public RetrofitControl retrofitControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
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
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void actionMe(String name) {

    }

    @Override
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;

    }

    @Override
    public void setAccessTokenValue(String accessToken) {
        this.accessToken.setAccessToken(accessToken);
    }

    @Override
    public DetailFragment getDetailFragment() {
        return detailFragment;
    }

    @Override
    public ListViewFragment getListViewFragment() {
        return null;
    }

    @Override
    public SubredditListViewFragment getSubredditListViewFragment() {
        return null;
    }

    @Override
    public RetrofitControl getRetrofitControl() {
        return retrofitControl;
    }
}
