package com.qartf.doseforreddit.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Bundle savedInstanceState;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        initNavigation();
        initComponents();
    }

    public abstract int getContentLayout();

    public abstract void initNavigation();

    public abstract void initComponents();
}
