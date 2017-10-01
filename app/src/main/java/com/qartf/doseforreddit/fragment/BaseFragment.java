package com.qartf.doseforreddit.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.qartf.doseforreddit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragment<T> extends Fragment {

    protected View rootView;
    protected T mCallback;
    protected SharedPreferences sharedPreferences;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.empty_title_text) TextView emptyTitleText;
    @BindView(R.id.empty_subtitle_text) TextView emptySubtitleText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (T) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentLayout(), container, false);
        ButterKnife.bind(this, rootView);
        emptyView.setVisibility(View.GONE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initComponents();
        return rootView;
    }

    public abstract int getContentLayout();

    public abstract void initComponents();


    protected boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    protected void setInfoServerIsBroken() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.server_problem));
        emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
    }

    protected void setInfoNoConnection() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.no_connection));
        emptySubtitleText.setText(getString(R.string.no_connection_sub_text));
    }

}
