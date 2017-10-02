package com.qartf.doseforreddit.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.adapter.SubredditAdapter;
import com.qartf.doseforreddit.model.Subreddit;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ART_F on 2017-09-05.
 */

public class SubredditListViewFragment extends
        BaseFragment<SubredditListViewFragment.ShubredditListViewFragmentInterface> implements SubredditAdapter.ListItemClickListener,
        SwipyRefreshLayout.OnRefreshListener {

    private GridLayoutManager layoutManager;
    private SubredditAdapter subredditAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new ArrayList<Subreddit>());
    }

    public void setAdapter(List<Subreddit> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        subredditAdapter = new SubredditAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(subredditAdapter);
    }


    public void onLoadFinished(SubredditParent subredditParent) {
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
                    mCallback.restoreDetailFragment();
                }
            }
        } else {
            if (checkConnection()) {
                setInfoServerIsBroken();
            } else {
                setInfoNoConnection();
            }
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Subreddit subreddit = (Subreddit) subredditAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.isSubscribe:
                String subscribe = subreddit.subscribers.equals("true") ? Constants.Subscribe.UN_SUB : Constants.Subscribe.SUB;
                mCallback.getRetrofitControl().postSubscribe(subscribe, subreddit.name);
                break;
            case R.id.subredditTitleFrame:
                mCallback.loadFragment(Constants.Id.SEARCH_POSTS);
                sharedPreferences.edit().putString(getString(R.string.pref_post_subreddit), subreddit.display_name).apply();
                break;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mCallback.getRetrofitControl().searchSubreddits();
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface ShubredditListViewFragmentInterface {
        RetrofitControl getRetrofitControl();

        void restoreDetailFragment();

        void loadFragment(int fragmentId);
    }
}