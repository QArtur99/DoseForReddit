package com.qartf.doseforreddit.view.activity;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Constants.Pref;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.fragment.SubmitFragment;

import javax.inject.Inject;

public class SubmitActivity extends BaseActivityChild implements SubmitFragment.SubmitFragmentInt {

    private SubmitFragment submitFragment;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
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
    public void switchToPostFragment() {
        onBackPressed();
    }
}
