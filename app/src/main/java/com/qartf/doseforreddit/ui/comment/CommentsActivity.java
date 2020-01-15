package com.qartf.doseforreddit.ui.comment;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.App;
import com.qartf.doseforreddit.ui.BaseActivityChild;
import com.qartf.doseforreddit.utility.Constants.Pref;
import com.qartf.doseforreddit.utility.Utility;

import javax.inject.Inject;

import butterknife.BindView;

public class CommentsActivity extends BaseActivityChild implements DetailFragment.DetailFragmentInt {

    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    @Inject
    CommentMVP.Presenter commentPresenter;
    @Inject
    SharedPreferences sharedPreferences;
    private Post post;
    private DetailFragment detailFragment;


    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initComponents() {
        ((App) getApplication()).getComponent().inject(this);
        getSupportActionBar().setTitle("Comments");
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

    @Override
    public void loginSnackBar() {
        Snackbar snackbar = Snackbar
                .make(mainActivityFrame, "You must be logged in", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void loadPosts() {
        onBackPressed();
    }
}
