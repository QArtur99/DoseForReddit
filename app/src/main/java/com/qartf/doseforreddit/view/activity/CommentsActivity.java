package com.qartf.doseforreddit.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.comment.CommentMVP;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Constants.Pref;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.fragment.DetailFragment;

import javax.inject.Inject;

public class CommentsActivity extends BaseActivityChild implements DetailFragment.DetailFragmentInt {

    private Post post;
    private DetailFragment detailFragment;

    CommentsActivity() {}

    @Inject
    CommentMVP.Presenter commentPresenter;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initComponents() {
        ((App) getApplication()).getComponent().inject(this);
        sharedPreferences.edit().putString(Pref.prefSecondFragment, Pref.prefDetailFragment).apply();
        if (Utility.isTablet(this)) {
            this.finish();
            return;
        }

        Intent intent = getIntent();
        post = new Gson().fromJson(intent.getStringExtra("link"), new TypeToken<Post>() {}.getType());
        commentPresenter.setPost(post);

        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        fragmentManager.beginTransaction()
                .add(R.id.detailsViewFrame, detailFragment)
                .commit();
    }
}
