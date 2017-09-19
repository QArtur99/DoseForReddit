package com.qartf.doseforreddit.fragmentControl;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.View;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.CommentsActivity;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.Subreddit;
import com.qartf.doseforreddit.utility.Constants;

public class ListViewFragmentControl implements ListViewFragment.OnImageClickListener {
    private MainActivity mainActivity;

    public ListViewFragmentControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void restoreDetailFragment() {
        if (mainActivity.postObject != null && mainActivity.findViewById(R.id.detailsViewFrame) != null) {
            loadDetailFragment();
            mainActivity.detailFragment.setPost(mainActivity.postObject);
        }
    }

    @Override
    public void onRefresh(int loaderId) {
        switch (loaderId) {
            case Constants.Id.POSTS:
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.POSTS, mainActivity.argsPost, mainActivity).forceLoad();
                break;
            case Constants.Id.SEARCH_POSTS:
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.SEARCH_POSTS, mainActivity.argsPost, mainActivity).forceLoad();
                break;
            case Constants.Id.SEARCH_SUBREDDITS:
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.SEARCH_SUBREDDITS, mainActivity.argsSubredit, mainActivity).forceLoad();
                break;
            case Constants.Id.COMMENTS:
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.COMMENTS, mainActivity.argsDetail, mainActivity).forceLoad();
                break;
        }

    }

    @Override
    public void onImageSelected(Object object, View view) {
        Subreddit subreddit;
        PostObject postObject;
        Bundle args;
        switch (view.getId()) {
            case R.id.commentsAction:
                postObject = (PostObject) object;
                if (mainActivity.findViewById(R.id.detailsViewFrame) != null) {
                    loadDetailFragment();
                    mainActivity.detailFragment.setPost(postObject);
                } else {
                    mainActivity.postObjectString = new Gson().toJson(postObject);
                    String tokenString = new Gson().toJson(mainActivity.accessToken);
                    Intent intent = new Intent(mainActivity, CommentsActivity.class);
                    intent.putExtra("link", mainActivity.postObjectString);
                    intent.putExtra("token", tokenString);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(mainActivity).toBundle();
                        mainActivity.startActivity(intent, bundle);
                    } else {
                        mainActivity.startActivity(intent);
                    }
                }
                break;
            case R.id.shareAction:
                postObject = (PostObject) object;
                mainActivity.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(mainActivity)
                        .setType("text/plain")
                        .setText(postObject.url)
                        .getIntent(), mainActivity.getResources().getString(R.string.action_share)));
                break;
            case R.id.isSubscribe:
                subreddit = (Subreddit) object;
                String subscribe = subreddit.subscribers.equals("true") ? Constants.Subscribe.UN_SUB : Constants.Subscribe.SUB;
                args = new Bundle();
                args.putString(Constants.Subscribe.SUBSCRIBE, subscribe);
                args.putString(Constants.Subscribe.FULLNAME, subreddit.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.SUBREDDIT_SUBSCRIBE, args, mainActivity).forceLoad();
                break;
            case R.id.subredditTitleFrame:
                mainActivity.loadFragment(Constants.Id.SEARCH_POSTS);
                subreddit = (Subreddit) object;
                mainActivity.sharedPreferences.edit().putString(mainActivity.getResources().getString(R.string.pref_post_subreddit), subreddit.display_name).apply();
                break;
            case R.id.upContainer:
                postObject = (PostObject) object;
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "1");
                args.putString(Constants.Vote.FULLNAME, postObject.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
            case R.id.downContainer:
                postObject = (PostObject) object;
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "-1");
                args.putString(Constants.Vote.FULLNAME, postObject.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
        }

    }

    public void loadDetailFragment() {
        if (mainActivity.findViewById(R.id.detailsViewFrame) != null) {
            if (mainActivity.detailFragment == null) {
                mainActivity.detailFragment = new DetailFragment();
            }
            mainActivity.fragmentManager.beginTransaction()
                    .replace(R.id.detailsViewFrame, mainActivity.detailFragment)
                    .commit();
        }
    }
}
