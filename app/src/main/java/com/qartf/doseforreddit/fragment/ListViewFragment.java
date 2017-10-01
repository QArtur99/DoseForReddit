package com.qartf.doseforreddit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.adapter.PostsAdapter;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.List;


public class ListViewFragment extends BaseFragment<ListViewFragment.ListViewFragmentInterface> implements PostsAdapter.ListItemClickListener,
        SwipyRefreshLayout.OnRefreshListener, View.OnTouchListener {

    private GridLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private String sortBy;
    private int loaderId;
    private Bundle bundle;
    private int firstView = 0;


    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        recyclerView.setOnTouchListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new ArrayList<Post>());
        bundle = getArguments();
        getBundleData();
    }

    private void getBundleData() {
        if (bundle != null) {
            firstView = sharedPreferences.getInt(getString(R.string.pref_firstView), 0);
        }
    }


    public void setAdapter(List<Post> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        postsAdapter = new PostsAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(postsAdapter);
    }


    public void onLoadFinished(PostParent postParent) {
        if (postParent != null) {
            List<Post> data = postParent.postList;
            loadingIndicator.setVisibility(View.GONE);

            if (postsAdapter != null) {
                postsAdapter.clearMovies();
            }


            if (data != null && !data.isEmpty()) {
                emptyView.setVisibility(View.GONE);


                postsAdapter.setMovies(data);
                if (bundle != null) {
                    if (Utility.isTablet(getContext())) {
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.restoreDetailFragment();
                            }
                        });
                    }
                    recyclerView.scrollToPosition(firstView);
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

    public int getFirstVisibleItemPosition() {
        return layoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            firstView = getFirstVisibleItemPosition();
            sharedPreferences.edit().putInt(getString(R.string.pref_firstView), getFirstVisibleItemPosition()).apply();
        }
        return false;
    }


    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Post post = (Post) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.imageContainer:
                Utility.startIntentPreview(getActivity(), post);
                break;
            case R.id.commentsAction:
                if (getActivity().findViewById(R.id.detailsViewFrame) != null) {
                    mCallback.loadDetailFragment();
                    mCallback.setPost(post);
                } else {
                    mCallback.startDetailActivity(post);
                }
                break;
            case R.id.shareAction:
                getActivity().startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(post.url)
                        .getIntent(), getActivity().getResources().getString(R.string.action_share)));
                break;
            case R.id.upContainer:
                mCallback.getRetrofitControl().postVote("1", post.name);
                break;
            case R.id.downContainer:
                mCallback.getRetrofitControl().postVote("-1", post.name);
                break;
        }

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mCallback.getRetrofitControl().getSubredditPosts();
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface ListViewFragmentInterface {
        RetrofitControl getRetrofitControl();

        void restoreDetailFragment();

        void loadDetailFragment();

        void setPost(Post post);

        void startDetailActivity(Post post);
    }
}
