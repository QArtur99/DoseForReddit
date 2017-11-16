package com.qartf.doseforreddit.view.activity;

import android.content.Intent;
import android.os.Handler;

import com.qartf.doseforreddit.R;

public class StartActivity extends BaseActivity{

    @Override
    public int getContentLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
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
