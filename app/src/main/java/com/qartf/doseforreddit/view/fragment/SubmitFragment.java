package com.qartf.doseforreddit.view.fragment;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.utility.Utility;

public class SubmitFragment extends BaseFragmentMvp {
    @Override
    public int getContentLayout() {
        return R.layout.fragment_submit;
    }

    @Override
    public void initComponents() {
        Utility.setTabLayoutDivider(getContext(), tabLayout);
        addTabLayoutTabs();

    }


    private void addTabLayoutTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("LINK"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("TEXT"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("IMAGE"), 2);
    }



}
