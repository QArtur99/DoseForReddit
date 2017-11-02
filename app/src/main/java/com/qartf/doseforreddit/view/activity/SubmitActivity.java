package com.qartf.doseforreddit.view.activity;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.view.fragment.SubmitFragment;

public class SubmitActivity extends BaseActivityChild {

    private SubmitFragment submitFragment;

    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initComponents() {
        ((App) getApplication()).getComponent().inject(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish();
            return;
        }
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        submitFragment = new SubmitFragment();
        fragmentManager.beginTransaction()
                .add(R.id.detailsViewFrame, submitFragment)
                .commit();
    }
}
