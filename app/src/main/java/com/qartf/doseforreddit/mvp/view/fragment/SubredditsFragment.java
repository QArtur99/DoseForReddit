package com.qartf.doseforreddit.mvp.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.mvp.data.entity.AccessToken;
import com.qartf.doseforreddit.mvp.data.entity.Subreddit;
import com.qartf.doseforreddit.mvp.data.entity.SubredditParent;
import com.qartf.doseforreddit.mvp.presenter.root.App;
import com.qartf.doseforreddit.mvp.presenter.subreddit.SubredditMVP;
import com.qartf.doseforreddit.mvp.presenter.utility.Constants;
import com.qartf.doseforreddit.mvp.view.adapter.SubredditAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SubredditsFragment extends BaseFragmentMvp<SubredditsFragment.SubredditsFragmentInt> implements SubredditMVP.View
        , SubredditAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    private GridLayoutManager layoutManager;
    private SubredditAdapter postsAdapter;

    @Inject
    SubredditMVP.Presenter presenter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new ArrayList<Subreddit>());
    }


    public void setAdapter(List<Subreddit> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        postsAdapter = new SubredditAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(postsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
    }


    @Override
    public void showToast(AccessToken accessToken) {
        Toast.makeText(getContext(), accessToken.accessToken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSubredditParent(SubredditParent postParent) {
        postsAdapter.clearMovies();
        emptyView.setVisibility(View.GONE);
        postsAdapter.setMovies(postParent.subredditList);
    }

    @Override
    public void error(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoadIndicatorOff() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Subreddit subreddit = (Subreddit) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.isSubscribe:
                String subscribe = subreddit.user_is_subscriber.equals("true") ? Constants.Subscribe.UN_SUB : Constants.Subscribe.SUB;
                presenter.postSubscribe(subscribe, subreddit.name);
                break;
            case R.id.subredditTitleFrame:
                mCallback.loadFragment(Constants.Id.SEARCH_POSTS);
                sharedPreferences.edit().putString(getString(R.string.pref_post_subreddit), subreddit.display_name).apply();
                break;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        presenter.loadSubreddits();
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface SubredditsFragmentInt {
        void loadFragment(int fragmentId);
    }

}
