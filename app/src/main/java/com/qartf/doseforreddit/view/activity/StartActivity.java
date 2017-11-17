package com.qartf.doseforreddit.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Constants;

import javax.inject.Inject;

public class StartActivity extends BaseActivity{

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getContentLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
        ((App) getApplication()).getComponent().inject(this);
        sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_HOME).apply();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                runMainUse();
            }
        }, 2000);

    }

    private void runMainUse() {
        Intent goToMainUse = new Intent(StartActivity.this, MainActivity.class);
        startActivityForResult(goToMainUse, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            StartActivity.this.finish();
        }
    }

}
