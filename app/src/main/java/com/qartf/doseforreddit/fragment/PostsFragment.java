package com.qartf.doseforreddit.fragment;

import android.os.Bundle;
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
import com.qartf.doseforreddit.utility.Navigation;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.List;


public class PostsFragment extends BaseFragment<PostsFragment.ListViewFragmentInterface> implements PostsAdapter.ListItemClickListener,
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
    }

    private void getBundleData() {
        if (bundle != null) {
            if (Utility.isTablet(getContext())) {
                mCallback.restoreDetailFragment();
            }
            firstView = sharedPreferences.getInt(getString(R.string.pref_firstView), 0);
            recyclerView.scrollToPosition(firstView);
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
                getBundleData();
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
                Navigation.startIntentPreview(getActivity(), post);
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
                Navigation.shareContent(getActivity(), post.url);
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
