package com.qartf.doseforreddit.mvp.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.mvp.data.model.AccessToken;
import com.qartf.doseforreddit.mvp.data.model.Post;
import com.qartf.doseforreddit.mvp.data.model.PostParent;
import com.qartf.doseforreddit.mvp.presenter.Navigator;
import com.qartf.doseforreddit.mvp.presenter.post.PostMVP;
import com.qartf.doseforreddit.mvp.presenter.root.App;
import com.qartf.doseforreddit.mvp.presenter.utility.Navigation;
import com.qartf.doseforreddit.mvp.view.adapter.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostsFragment extends BaseFragmentMvp<PostsFragment.PostsFragmentInt> implements PostMVP.View
        , PostsAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    public GridLayoutManager layoutManager;
    public PostsAdapter postsAdapter;

    @Inject
    PostMVP.Presenter presenter;
    @Inject
    Navigator navigator;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        this.navigator.setActivity(getActivity());



        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new ArrayList<Post>());
    }

    public void setAdapter(List<Post> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        postsAdapter = new PostsAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(postsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
        presenter.loadPosts();
    }

    @Override
    public void showToast(AccessToken accessToken) {
        Toast.makeText(getContext(), accessToken.accessToken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPostParent(PostParent postParent) {
        postsAdapter.clearMovies();
        emptyView.setVisibility(View.GONE);
        postsAdapter.setMovies(postParent.postList);
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
        Post post = (Post) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.imageContainer:
                Navigation.startIntentPreview(getActivity(), post);
                break;
            case R.id.commentsAction:
                if (getActivity().findViewById(R.id.detailsViewFrame) != null) {
                    mCallback.setPost(post);
                    mCallback.loadDetailFragment();
                } else {
                    navigator.startDetailActivity(post);
                }
                break;
            case R.id.shareAction:
                Navigation.shareContent(getActivity(), post.url);
                break;
            case R.id.upContainer:
                presenter.postVote("1", post.name);
                break;
            case R.id.downContainer:
                presenter.postVote("-1", post.name);
                break;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        presenter.loadPosts();
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface PostsFragmentInt {

        void loadDetailFragment();

        void setPost(Post post);
    }

}
