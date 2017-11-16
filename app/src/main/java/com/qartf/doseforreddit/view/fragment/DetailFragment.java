package com.qartf.doseforreddit.view.fragment;

import android.os.Handler;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;
import com.qartf.doseforreddit.presenter.comment.CommentMVP;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Navigation;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.adapter.CommentsAdapter;
import com.qartf.doseforreddit.view.dialog.CommentSettingsDialog;
import com.qartf.doseforreddit.view.dialog.PostDetailSettings;
import com.qartf.doseforreddit.view.dialog.QuickReplyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

public class DetailFragment extends BaseFragmentMvp<DetailFragment.DetailFragmentInt> implements CommentMVP.View, CommentsAdapter.OnListItemClickListener,
        View.OnClickListener, SwipyRefreshLayout.OnRefreshListener, QuickReplyDialog.QuickReplyInter, CommentSettingsDialog.CommentSettingsInter,
        PostDetailSettings.PostDetailSettingsInter {


    @BindView(R.id.thumbnail) ImageView thumbnail;
    @BindView(R.id.postDetialsSettings) ImageView postDetialsSettings;
    @BindView(R.id.ups) TextView ups;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subreddit) TextView subreddit;
    @BindView(R.id.linkFlairText) TextView linkFlairText;
    @BindView(R.id.domain) TextView domain;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.upContainer) RelativeLayout upContainer;
    @BindView(R.id.upArrow) ImageView upArrow;
    @BindView(R.id.downContainer) RelativeLayout downContainer;
    @BindView(R.id.downArrow) ImageView downArrow;
    @BindView(R.id.detailContainer) RelativeLayout detailContainer;
    @BindView(R.id.imageContainer) RelativeLayout imageContainer;
    @BindView(R.id.selftext) TextView selftext;
    @BindView(R.id.commentsNo) TextView commentsNo;
    @BindView(R.id.loading_indicator) ProgressBar progressBar;
    @BindView(R.id.mainFrame) LinearLayout fragmentFrame;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
    @BindView(R.id.spinnerSortBy) Spinner spinnerSortBy;
    @BindString(R.string.pref_post_detail_sub) String prefPostDetailSub;
    @BindString(R.string.pref_post_detail_id) String prefPostDetailId;
    @BindString(R.string.pref_post_detail_sort_key) String prefPostDetailSortKey;

    private HashMap<String, String> spinnerMap;
    private boolean isOpen = false;

    private CommentsAdapter commentsAdapter;
    private CommentsAdapter commentsChildAdapter;
    private LinearLayout previousViewSelected;
    private LinearLayout previousViewExpanded;
    private ImageView saveStar;
    private Comment comment;


    @Inject
    CommentMVP.Presenter presenter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        emptyView.setVisibility(View.GONE);
        swipyRefreshLayout.setOnRefreshListener(this);

        Utility.setThumbnailSize(getActivity(), fragmentFrame);
        setListeners();
        setAdapter(new ArrayList<Comment>());
        setSpinner();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
        presenter.loadPostData();

    }

    private void setListeners() {
        upContainer.setOnClickListener(this);
        downContainer.setOnClickListener(this);
        detailContainer.setOnClickListener(this);
        imageContainer.setOnClickListener(this);
        postDetialsSettings.setOnClickListener(this);
    }

    public void setUps(String upsString){
        ups.setText(upsString);
    }

    @Override
    public void setLikes(String postLikes) {
        if(postLikes.equals("true")){
            upArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.upArrow));
        }else if(postLikes.equals("false")){
            downArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.downArrow));
        }
    }

    public void setTitle(String titleString){
        title.setText(titleString);
    }

    public void setLinkFlairText(String linkFlairTextString){
        Utility.loadLinkFlairText(linkFlairText, linkFlairTextString);
    }

    public void setDomain(String domainString){
        domain.setText(domainString);
    }

    public void setSubreddit(String subredditString){
        subreddit.setText(subredditString);
    }

    public void setComments(String commentsString){
        comments.setText(commentsString);
    }

    public void setTime(String timeString){
        time.setText(timeString);
    }

    public void setThumbnail(Post post){
        Utility.loadThumbnail(getContext(), post, thumbnail);
    }

    public void setSelftext(String selftextString){
        if (selftextString != null && !selftextString.isEmpty()) {
            selftext.setVisibility(View.VISIBLE);
            selftext.setText(selftextString);
        }
    }

    public void setCommentsNo(String commentsNoString){
        commentsNo.setText(commentsNoString);
    }

    @Override
    public void setSaveStarActivated() {
        if(saveStar != null){
            comment.saved = "true";
            saveStar.setColorFilter(ContextCompat.getColor(getContext(), R.color.commentSave));
        }
    }

    @Override
    public void setSaveStarUnActivated() {
        if(saveStar != null){
            comment.saved = "false";
            saveStar.setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
        }
    }

    private void setSpinner() {
        spinnerMap = new HashMap<>();
        String[] spinnerArray = getResources().getStringArray(R.array.sortComments);
        String[] spinnerArrayValue = getResources().getStringArray(R.array.sortValueComment);
        for (int i = 0; i < spinnerArray.length; i++) {
            spinnerMap.put(spinnerArray[i], spinnerArrayValue[i]);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, spinnerArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(arrayAdapter);
        int lastSelected = sharedPreferences.getInt(getResources().getString(R.string.pref_comment_sort_by), 0);
        spinnerSortBy.setSelection(lastSelected);
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sharedPreferences.edit().putInt(getResources().getString(R.string.pref_comment_sort_by), i).apply();
                String sortBy = (String) adapterView.getAdapter().getItem(i);
                String sortKey = spinnerMap.get(sortBy);
                sharedPreferences.edit().putString(prefPostDetailSortKey, sortKey).apply();
                loadComments();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    @Override
    public void loadComments() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences.edit().putString(prefPostDetailId, presenter.getPost().id).apply();
                sharedPreferences.edit().putString(prefPostDetailSub, presenter.getPost().subreddit).apply();
                presenter.loadComments();
            }
        }, 1000);
    }

    @Override
    public void loadPosts() {
                mCallback.loadPosts();
    }

    public void setAdapter(List<Comment> movieList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        commentsAdapter = new CommentsAdapter(getActivity(), movieList, this);
        recyclerView.setAdapter(commentsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upContainer:
                upArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.upArrow));
                downArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
                presenter.postVote("1", presenter.getPost().name);
                break;
            case R.id.downContainer:
                upArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.arrowColor));
                downArrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.downArrow));
                presenter.postVote("-1", presenter.getPost().name);
                break;
            case R.id.detailContainer:
                Navigation.startLinkActivity(getActivity(), presenter.getPost().url);
                break;
            case R.id.imageContainer:
                Navigation.startIntentPreview(getActivity(), presenter.getPost());
                break;
            case R.id.postDetialsSettings:
                new PostDetailSettings(getActivity(), presenter.getPost(), this, this);
//                Navigation.shareContent(getActivity(), presenter.getPost().url);
                break;
        }
    }


    @Override
    public void onCommentListItemClick(Comment comment, CommentsAdapter commentsAdapter, View view) {
        switch (view.getId()) {
            case R.id.moreSettings:
                new CommentSettingsDialog(getActivity(), comment, this);
                break;

            case R.id.reply:
                new QuickReplyDialog(getContext(), comment.name,this);
                break;
            case R.id.save:
                this.comment = comment;
                this.saveStar = (ImageView) view;
                if(comment.saved.equals("true")){
                    presenter.postUnsave(comment.name);
                }else {
                    presenter.postSave(comment.name);
                }
                break;
            case R.id.loadMore:
                this.commentsChildAdapter = commentsAdapter;
                comment.parentId = presenter.getPost().name;
                presenter.loadChildComments(comment);
                break;
            case R.id.commentVoteUp:
                presenter.postVote("1", comment.name);
                break;
            case R.id.commentVoteDown:
                presenter.postVote("-1", comment.name);
                break;
        }
    }

    @Override
    public void onCommentSelected(int clickedItemIndex, View expandableView, View parent) {
        if (previousViewSelected != null) {
            previousViewSelected.setActivated(false);
        }
        previousViewSelected = (LinearLayout) parent;
        parent.setActivated(true);
        actionDetail((LinearLayout) expandableView);
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

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        presenter.loadComments();
        swipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(AccessToken accessToken) {
        Toast.makeText(getContext(), accessToken.accessToken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCommentParent(CommentParent postParent) {
        commentsAdapter.clearMovies();
        emptyView.setVisibility(View.GONE);
        commentsAdapter.setMovies(postParent.commentList);
    }

    @Override
    public void setChildCommentParent(ChildCommentParent postParent) {
        commentsChildAdapter.setMovies(postParent.json.data.commentList);
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
    public void submitComment(String fullname, String submitText) {
        presenter.postComment(fullname, submitText);
    }

    @Override
    public void removeComment(String fullname) {
        presenter.postDel(fullname);
    }

    @Override
    public void removePost(String fullname) {
        presenter.postDelPost(fullname);
    }




    public interface DetailFragmentInt {
        void loadPosts();
    }
}
