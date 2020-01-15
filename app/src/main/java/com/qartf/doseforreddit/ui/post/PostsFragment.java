package com.qartf.doseforreddit.ui.post;

import androidx.transition.TransitionManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.App;
import com.qartf.doseforreddit.ui.BaseFragmentMvp;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Navigation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostsFragment extends BaseFragmentMvp<PostsFragment.PostsFragmentInt> implements PostMVP.View
        , PostsAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    public GridLayoutManager layoutManager;
    public PostsAdapter postsAdapter;
    @Inject
    PostMVP.Presenter presenter;
    private PostParent postParent;
    private int postLoaderId;
    private LinearLayout previousViewSelected;
    private LinearLayout previousViewExpanded;
    private boolean isOpen = false;
    private int selectedPosition;
    private Post post;
    private ImageView saveStar;
    private String logged;
    private View voteConteinerView;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        postLoaderId = sharedPreferences.getInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_HOME);
        emptyView.setVisibility(View.GONE);
        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new ArrayList<>());
    }

    public void setAdapter(List<Object> movieList) {
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
        postsAdapter.clearPosts();
        loadPosts("");
    }

    @Override
    public void showToast(AccessToken accessToken) {
        Toast.makeText(getContext(), accessToken.accessToken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPostParent(PostParent postParent) {
        this.postParent = postParent;
        emptyView.setVisibility(View.GONE);

        swipyRefreshLayout.setRefreshing(false);
    }

    public boolean checkUserIsLogged(){
        boolean user = false;
        logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        if (logged.equals(Constants.Utility.ANONYMOUS)) {
            mCallback.loginSnackBar();
            user = false;
        } else {
            user = true;
        }
        return user;
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
    public void setSaveStarActivated() {
        if(saveStar != null){
            post.saved = "true";
            saveStar.setColorFilter(ContextCompat.getColor(getContext(), R.color.commentSave));
        }
    }

    @Override
    public void setSaveStarUnActivated() {
        if(saveStar != null){
            post.saved = "false";
            saveStar.setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
        }
    }

    @Override
    public void setRefreshing() {
        if(swipyRefreshLayout != null) {
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        voteConteinerView = view;
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
            case R.id.save:
                if(checkUserIsLogged()){
                    this.post = post;
                    this.saveStar = (ImageView) view;
                    if (post.saved.equals("true")) {
                        presenter.postUnsave(post.name);
                    } else {
                        presenter.postSave(post.name);
                    }
                }
                break;
            case R.id.goToUrl:
                Navigation.startLinkActivity(getActivity(), post.url);
                break;
            case R.id.shareAction:
                Navigation.shareContent(getActivity(), post.url);
                break;
            case R.id.upContainer:
                if(checkUserIsLogged()){
                    presenter.postVote("1", post.name);
                }
                break;
            case R.id.downContainer:
                if(checkUserIsLogged()){
                    presenter.postVote("-1", post.name);
                }
                break;
        }
    }

    @Override
    public void setVote(){
        RelativeLayout view = (RelativeLayout) voteConteinerView.getParent();
        switch (voteConteinerView.getId()){
            case R.id.upContainer:
                ((ImageView)view.findViewById(R.id.upArrow)).setColorFilter(ContextCompat.getColor(getContext(), R.color.upArrow));
                ((ImageView)view.findViewById(R.id.downArrow)).setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
                break;
            case R.id.downContainer:
                ((ImageView)view.findViewById(R.id.upArrow)).setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
                ((ImageView)view.findViewById(R.id.downArrow)).setColorFilter(ContextCompat.getColor(getContext(), R.color.downArrow));
                break;
        }

    }

    public int getPostType() {
        return postLoaderId;
    }

    public void setPostType(int postLoaderId) {
        this.postLoaderId = postLoaderId;
    }

    public void clearAdapter() {
        if (postsAdapter != null) {
            postsAdapter.clearPosts();
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {

            if (TextUtils.isEmpty(postParent.after)) {
                swipyRefreshLayout.setRefreshing(false);
                error("There are no more posts to show right now.");
                return;
            }

            loadPosts(postParent.after);

        } else if (direction == SwipyRefreshLayoutDirection.TOP) {
            postsAdapter.clearPosts();
            loadPosts("");
        }
    }

    @Override
    public void onPostSelected(int clickedItemIndex, View expandableView, View parent) {
        selectedPosition = clickedItemIndex;
        if (previousViewSelected != null) {
            previousViewSelected.setActivated(false);
        }
        previousViewSelected = (LinearLayout) parent;
        parent.setActivated(true);
        actionDetail((LinearLayout) expandableView);
    }

    @Override
    public int getSelectedPosition() {
        return selectedPosition;
    }

    private void actionDetail(final LinearLayout expandArea) {
        if (previousViewExpanded != null) {
            previousViewExpanded.setVisibility(View.GONE);
            previousViewExpanded.setActivated(false);
        }

        if (previousViewExpanded == null || !previousViewExpanded.equals(expandArea) || isOpen) {
            expandArea.setVisibility(View.VISIBLE);
            expandArea.setActivated(true);
            expandArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionManager.beginDelayedTransition(expandArea);
                }
            });
            isOpen = false;
        } else {
            isOpen = true;
        }
        previousViewExpanded = expandArea;
    }


    private void loadPosts(String after) {
        switch (postLoaderId){
            case Constants.PostLoaderId.POST_HOME:
                presenter.loadHome(after);
                break;
            case Constants.PostLoaderId.POST_VIEW:
                presenter.loadPosts(after);
                break;
            case Constants.PostLoaderId.SEARCH_POSTS:
                presenter.searchPosts(after);
                break;
        }
    }

    public interface PostsFragmentInt {

        void loginSnackBar();

        void loadDetailFragment();

        void setPost(Post post);
    }

}
