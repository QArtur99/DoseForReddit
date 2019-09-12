package com.qartf.doseforreddit.view.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Subreddit;
import com.qartf.doseforreddit.data.entity.SubredditParent;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.subreddit.SubredditMVP;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.view.adapter.SubredditAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;

public class SubredditsFragment extends BaseFragmentMvp<SubredditsFragment.SubredditsFragmentInt> implements SubredditMVP.View
        , SubredditAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    @BindString(R.string.subscribe) String subsribe;
    @BindString(R.string.unsubscribe) String unsubscribe;
    @Inject
    SubredditMVP.Presenter presenter;
    private GridLayoutManager layoutManager;
    private SubredditAdapter subredditAdapter;
    private SubredditParent subredditParent;
    private Subreddit subreddit;
    private TextView isSubscribe;
    private Boolean isMine = false;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_VIEW).apply();
        emptyView.setVisibility(View.GONE);
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
        this.subredditParent = postParent;
        emptyView.setVisibility(View.GONE);
        subredditAdapter.setMovies(postParent.subredditList);
        swipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshing() {
        if (swipyRefreshLayout != null) {
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setPostSubscribe() {
        if (isSubscribe.isActivated()) {
            isSubscribe.setActivated(false);
            isSubscribe.setText(subsribe);
            subreddit.user_is_subscriber = "false";
        } else {
            isSubscribe.setActivated(true);
            isSubscribe.setText(unsubscribe);
            subreddit.user_is_subscriber = "true";
        }
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
        subreddit = (Subreddit) subredditAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.isSubscribe:
                String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
                if (logged.equals(Constants.Utility.ANONYMOUS)) {
                    mCallback.loginSnackBar();
                } else {
                    this.isSubscribe = (TextView) view;
                    String subscribe = subreddit.user_is_subscriber.equals("true") ? Constants.Subscribe.UN_SUB : Constants.Subscribe.SUB;
                    presenter.postSubscribe(subscribe, subreddit.name);
                }
                break;
            case R.id.subredditTitleFrame:
                mCallback.loadFragment(Constants.Id.SEARCH_POSTS);
                sharedPreferences.edit().putString(getString(R.string.pref_post_subreddit), subreddit.display_name).apply();
                mCallback.setTitle("/r/" + subreddit.display_name);
                break;
        }
    }

    public void clearAdapter() {
        if (subredditAdapter != null) {
            subredditAdapter.clearSubreddits();
        }
    }

    public void setSubredditType(Boolean isMine) {
        this.isMine = isMine;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {

            if (TextUtils.isEmpty(subredditParent.after)) {
                swipyRefreshLayout.setRefreshing(false);
                error("There are no more subreddits to show right now.");
                return;
            }

            loadSubreddits(subredditParent.after);

        } else if (direction == SwipyRefreshLayoutDirection.TOP) {
            subredditAdapter.clearSubreddits();
            loadSubreddits("");
        }
    }

    private void loadSubreddits(String after) {
        if (isMine) {
            presenter.loadMineSubreddits(after);
        } else {
            presenter.loadSubreddits(after);
        }
    }

    public interface SubredditsFragmentInt {
        void loginSnackBar();

        void loadFragment(int fragmentId);

        void setTitle(String title);
    }

}
