package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.network.DataLoader;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Id;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,
        DetailFragment.OnListItemClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;



    private PostObject post;
    private AccessToken accessToken;
    private FragmentManager fragmentManager;
    private DetailFragment detailFragment;

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

        Intent intent = getIntent();
        post = new Gson().fromJson(intent.getStringExtra("link"), new TypeToken<PostObject>() {}.getType());
        accessToken = new Gson().fromJson(intent.getStringExtra("token"), AccessToken.class);

        fragmentManager = getSupportFragmentManager();
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
    public void getComments(PostObject post, String sortBy) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Comments.SUBREDDIT, post.subreddit);
        bundle.putString(Constants.Comments.ID, post.id);
        bundle.putString(Constants.Comments.SORT, sortBy);
        getSupportLoaderManager().restartLoader(Constants.Id.COMMENTS, bundle, this).forceLoad();
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
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Id.COMMENTS:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.COMMENTS);
            case Id.VOTE:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.VOTE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case Id.COMMENTS:
                detailFragment.onLoadFinished(loader, (List<Comment>) data);
                break;
            case Id.VOTE:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(PostObject post, View view) {
        Bundle args;
        switch (view.getId()) {
            case R.id.upContainer:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "1");
                args.putString(Constants.Vote.FULLNAME, post.name);
                getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, this).forceLoad();
                break;
            case R.id.downContainer:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "-1");
                args.putString(Constants.Vote.FULLNAME, post.name);
                getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, this).forceLoad();
                break;
        }
    }

    @Override
    public void onCommentListItemClick(Comment comment, View view) {
        Bundle args;
        switch (view.getId()) {
            case R.id.commentVoteUp:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "1");
                args.putString(Constants.Vote.FULLNAME, comment.name);
                getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, this).forceLoad();
                break;
            case R.id.commentVoteDown:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "-1");
                args.putString(Constants.Vote.FULLNAME, comment.name);
                getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, this).forceLoad();
                break;
        }

    }

}
