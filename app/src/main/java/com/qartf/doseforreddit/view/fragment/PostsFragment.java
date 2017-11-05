package com.qartf.doseforreddit.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.presenter.post.PostMVP;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Navigation;
import com.qartf.doseforreddit.view.adapter.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostsFragment extends BaseFragmentMvp<PostsFragment.PostsFragmentInt> implements PostMVP.View
        , PostsAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    public GridLayoutManager layoutManager;
    public PostsAdapter postsAdapter;
    private PostParent postParent;
    private Boolean isSearch = false;

    @Inject
    PostMVP.Presenter presenter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        emptyView.setVisibility(View.GONE);
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
        presenter.loadPosts("");
    }

    @Override
    public void showToast(AccessToken accessToken) {
        Toast.makeText(getContext(), accessToken.accessToken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPostParent(PostParent postParent) {
        this.postParent = postParent;
        emptyView.setVisibility(View.GONE);
        postsAdapter.setPosts(postParent.postList);
        swipyRefreshLayout.setRefreshing(false);
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
                    Navigation.startDetailActivity(getActivity(), post);
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

    public void setPostType(Boolean isSearch){
        this.isSearch = isSearch;
    }

    public Boolean getPostType(){
        return isSearch;
    }

    public void clearAdapter(){
        if(postsAdapter != null) {
            postsAdapter.clearPosts();
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.BOTTOM) {

            if(TextUtils.isEmpty(postParent.after)){
                swipyRefreshLayout.setRefreshing(false);
                error("There are no more posts to show right now.");
                return;
            }

            loadPosts(postParent.after);

        }else if(direction == SwipyRefreshLayoutDirection.TOP) {
            postsAdapter.clearPosts();
            loadPosts("");
        }
    }

    private void loadPosts(String after) {
        if(isSearch){
            presenter.searchPosts(after);
        }else{
            presenter.loadPosts(after);
        }
    }

    public interface PostsFragmentInt {

        void loadDetailFragment();

        void setPost(Post post);
    }

}
