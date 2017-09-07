package com.qartf.doseforreddit.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.adapter.SubredditAdapter;
import com.qartf.doseforreddit.model.Subreddit;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ART_F on 2017-09-05.
 */

public class SubredditListViewFragment extends Fragment implements SubredditAdapter.ListItemClickListener,
        SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.empty_title_text) TextView emptyTitleText;
    @BindView(R.id.empty_subtitle_text) TextView emptySubtitleText;

    private MainActivity mainActivity;
    private GridLayoutManager layoutManager;
    private SubredditAdapter subredditAdapter;
    private OnImageClickListener mCallback;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, rootView);
        swipyRefreshLayout.setOnRefreshListener(this);

        emptyView.setVisibility(View.GONE);
        mainActivity = ((MainActivity) getActivity());
        setAdapter(new ArrayList<Subreddit>());
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public void setAdapter(List<Subreddit> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        subredditAdapter = new SubredditAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(subredditAdapter);
    }


    public void onLoadFinished(Loader loader, SubredditParent subredditParent) {
        if (subredditParent != null) {
            List<Subreddit> data = subredditParent.subredditList;
            loadingIndicator.setVisibility(View.GONE);

            if (subredditAdapter != null) {
                subredditAdapter.clearMovies();
            }


            if (data != null && !data.isEmpty()) {
                emptyView.setVisibility(View.GONE);
                subredditAdapter.setMovies(data);
                if (Utility.isTablet(getContext())) {
                    rootView.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.restoreDetailFragment();
                        }
                    });
                }
            }
        } else {
            if (checkConnection()) {
                emptyView.setVisibility(View.VISIBLE);
                emptyTitleText.setText(getString(R.string.server_problem));
                emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
            } else {
                setInfoNoConnection();
            }
        }
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void setInfoNoConnection() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.no_connection));
        emptySubtitleText.setText(getString(R.string.no_connection_sub_text));
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Subreddit subreddit = (Subreddit) subredditAdapter.getDataAtPosition(clickedItemIndex);
        mCallback.onImageSelected(subreddit, view);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mCallback.onRefresh(Constants.Id.SEARCH_SUBREDDITS);
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface OnImageClickListener {
        void restoreDetailFragment();
        void onRefresh(int loaderId);
        void onImageSelected(Object movie, View view);
    }
}