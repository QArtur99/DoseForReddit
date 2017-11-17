package com.qartf.doseforreddit.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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

public abstract class BaseFragmentMvp<T> extends Fragment {
    @Nullable
    @BindView(R.id.loading_indicator)
    public ProgressBar loadingIndicator;
    @Nullable
    @BindView(R.id.emptyView)
    public RelativeLayout emptyView;
    @Nullable
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    public SwipyRefreshLayout swipyRefreshLayout;
    @Nullable
    @BindView(R.id.empty_title_text)
    public TextView emptyTitleText;
    @Nullable
    @BindView(R.id.empty_subtitle_text)
    public TextView emptySubtitleText;
    protected View rootView;
    protected T mCallback;
    protected SharedPreferences sharedPreferences;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initComponents();
        return rootView;
    }

    public abstract int getContentLayout();

    public abstract void initComponents();

    public void setInfoServerIsBroken() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.server_problem));
        emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
    }

    public void setInfoNoConnection() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.no_connection));
        emptySubtitleText.setText(getString(R.string.no_connection_sub_text));
    }

    public void setRecyclerView() {
        if (recyclerView != null && loadingIndicator != null && emptyView != null) {
            recyclerView.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

}
