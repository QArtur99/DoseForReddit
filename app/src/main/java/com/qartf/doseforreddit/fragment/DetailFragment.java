package com.qartf.doseforreddit.fragment;

import android.content.Intent;
import android.support.transition.TransitionManager;
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

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.adapter.CommentsAdapter;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by ART_F on 2017-09-06.
 */

public class DetailFragment extends BaseFragment<DetailFragment.DetailFragmentInterface> implements CommentsAdapter.OnListItemClickListener,
        View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {


    public static final String DOT = "\u2022";
    @BindView(R.id.thumbnail) ImageView thumbnail;
    @BindView(R.id.shareAction) ImageView shareAction;
    @BindView(R.id.ups) TextView ups;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subreddit) TextView subreddit;
    @BindView(R.id.linkFlairText) TextView linkFlairText;
    @BindView(R.id.domain) TextView domain;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.upContainer) RelativeLayout upContainer;
    @BindView(R.id.downContainer) RelativeLayout downContainer;
    @BindView(R.id.detailContainer) RelativeLayout detailContainer;
    @BindView(R.id.imageContainer) RelativeLayout imageContainer;
    @BindView(R.id.selftext) TextView selftext;
    @BindView(R.id.commentsNo) TextView commentsNo;
    @BindView(R.id.loading_indicator) ProgressBar progressBar;
    @BindView(R.id.fragmentFrame) LinearLayout fragmentFrame;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
    @BindView(R.id.spinnerSortBy) Spinner spinnerSortBy;
    @BindString(R.string.pref_post_detail_sub) String prefPostDetailSub;
    @BindString(R.string.pref_post_detail_id) String prefPostDetailId;
    @BindString(R.string.pref_post_detail_sort_key) String prefPostDetailSortKey;

    private Post post;
    private HashMap<String, String> spinnerMap;
    private boolean isOpen = false;

    private CommentsAdapter commentsAdapter;
    private LinearLayout previousViewSelected;
    private LinearLayout previousViewExpanded;


    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void initComponents() {
        swipyRefreshLayout.setOnRefreshListener(this);

        Utility.setThumbnailSize(getActivity(), fragmentFrame);
        setListeners();
        setAdapter(new ArrayList<Comment>());
        setData();
    }

    private void setData() {
        if (post != null) {
            loadPostData();
            setSpinner();
        }
    }

    private void setListeners() {
        upContainer.setOnClickListener(this);
        downContainer.setOnClickListener(this);
        detailContainer.setOnClickListener(this);
        imageContainer.setOnClickListener(this);
        shareAction.setOnClickListener(this);
    }

    public void setPost(Post post) {
        this.post = post;
        if (getContext() != null) {
            setData();
        }
    }

    private void loadPostData() {
        Utility.upsFormat(ups, Integer.valueOf(post.ups));
        title.setText(post.title);
        Utility.loadLinkFlairText(linkFlairText, post.linkFlairText);
        domain.setText("(" + post.domain + ")");
        subreddit.setText(post.subreddit);
        comments.setText(DOT + post.numComents + " comments");
        Utility.timeFormat(post.createdUTC, time);
        Utility.loadThumbnail(getContext(), post, thumbnail);

        if (post.selftext != null && !post.selftext.isEmpty()) {
            selftext.setVisibility(View.VISIBLE);
            selftext.setText(post.selftext);
        }

        String commentsNoString = post.numComents + " comments";
        commentsNo.setText(commentsNoString);
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

                sharedPreferences.edit().putString(prefPostDetailId, post.id).apply();
                sharedPreferences.edit().putString(prefPostDetailSortKey, sortKey).apply();
                sharedPreferences.edit().putString(prefPostDetailSub, post.subreddit).apply();
                mCallback.getRetrofitControl().getComments();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    public void setAdapter(List<Comment> movieList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        commentsAdapter = new CommentsAdapter(getActivity(), movieList, this);
        recyclerView.setAdapter(commentsAdapter);
    }

    public void onLoadFinished(List<Comment> data) {
        progressBar.setVisibility(View.GONE);
        if (commentsAdapter != null) {
            commentsAdapter.clearMovies();
        }

        if (data != null && !data.isEmpty()) {
            commentsAdapter.setMovies(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upContainer:
                mCallback.getRetrofitControl().postVote("1", post.name);
                break;
            case R.id.downContainer:
                mCallback.getRetrofitControl().postVote("-1", post.name);
                break;
            case R.id.detailContainer:
                Intent intent = new Intent(getActivity(), LinkActivity.class);
                intent.putExtra("link", post.url);
                startActivity(intent);
                break;
            case R.id.imageContainer:
                Utility.startIntentPreview(getActivity(), post);
                break;
            case R.id.shareAction:
                Utility.shareContent(getActivity(), post.url);
                break;
        }
    }

    @Override
    public void onCommentListItemClick(int clickedItemIndex, View view) {
        Comment comment = (Comment) commentsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.commentVoteUp:
                mCallback.getRetrofitControl().postVote("1", comment.name);
                break;
            case R.id.commentVoteDown:
                mCallback.getRetrofitControl().postVote("-1", comment.name);
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
        mCallback.getRetrofitControl().getComments();
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface DetailFragmentInterface {
        RetrofitControl getRetrofitControl();
    }
}
