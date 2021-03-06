package com.qartf.doseforreddit.ui.submit;

import android.content.SharedPreferences;
import androidx.fragment.app.FragmentManager;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.App;
import com.qartf.doseforreddit.ui.BaseActivityChild;
import com.qartf.doseforreddit.utility.Constants.Pref;
import com.qartf.doseforreddit.utility.Utility;

import javax.inject.Inject;

public class SubmitActivity extends BaseActivityChild implements SubmitFragment.SubmitFragmentInt {

    private SubmitFragment submitFragment;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getContentLayout() {
        return R.layout.activity_submit;
    }

    @Override
    public void initComponents() {
        ((App) getApplication()).getComponent().inject(this);
        sharedPreferences.edit().putString(Pref.prefSecondFragment, Pref.prefSubmitFragment).apply();
        if (Utility.isTablet(this)) {
            this.finish();
            return;
        }
        getSupportActionBar().setTitle("Submit");
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        submitFragment = new SubmitFragment();
        fragmentManager.beginTransaction()
                .add(R.id.detailsViewFrame, submitFragment)
                .commit();
    }

    @Override
    public void loadDetailFragment() {

    }

    @Override
    public void setPost(Post post) {

    }

    @Override
    public void switchToPostFragment() {
        onBackPressed();
    }
}
