package com.qartf.doseforreddit.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.qartf.doseforreddit.R;

import butterknife.BindView;

public abstract class BaseActivityChild extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    BaseActivityChild() {}

    @Override
    public void initNavigation() {
        setToolbar();
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

}
