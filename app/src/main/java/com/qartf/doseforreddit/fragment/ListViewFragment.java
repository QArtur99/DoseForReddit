package com.qartf.doseforreddit.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.CommentsActivity;
import com.qartf.doseforreddit.activity.ImageActivity;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.activity.SelfActivity;
import com.qartf.doseforreddit.activity.VideoActivity;
import com.qartf.doseforreddit.adapter.PostsAdapter;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListViewFragment extends Fragment implements PostsAdapter.ListItemClickListener {

    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.empty_title_text) TextView emptyTitleText;
    @BindView(R.id.empty_subtitle_text) TextView emptySubtitleText;

    private MainActivity mainActivity;
    private GridLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private String sortBy;
    private OnImageClickListener mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, rootView);
        emptyView.setVisibility(View.GONE);
        mainActivity = ((MainActivity) getActivity());
        setAdapter(new ArrayList<PostObject>());
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public void setAdapter(List<PostObject> movieList) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        postsAdapter = new PostsAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(postsAdapter);
    }


    public void onLoadFinished(Loader loader, PostObjectParent x) {
        List<PostObject> data = x.postObjectList;
        loadingIndicator.setVisibility(View.GONE);
        if (true) {
            if (postsAdapter != null) {
                postsAdapter.clearMovies();
            }
        }

        if (data != null && !data.isEmpty()) {
            emptyView.setVisibility(View.GONE);


            postsAdapter.setMovies(data);

        } else {
            emptyView.setVisibility(View.VISIBLE);
            if (sortBy.equals(getString(R.string.no_favorite))) {
                emptyTitleText.setText(getString(R.string.no_favorite));
                emptySubtitleText.setText(getString(R.string.no_favorite_sub_text));
            } else {
                emptyTitleText.setText(getString(R.string.server_problem));
                emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
            }
        }


    }


    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Intent intent = null;
        String link = "";
        PostObject post = (PostObject) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.commentsAction:
                mCallback.onImageSelected(post, view);
                break;
            case R.id.imageContainer:
                String postHint = post.postHint;
                if (!post.previewGif.isEmpty()) {
                    String to_remove = "amp;";
                    link = post.previewGif.replace(to_remove, "");
                    intent = new Intent(getActivity(), VideoActivity.class);
                } else if (postHint.equals("link")) {
                    String to_remove = "amp;";
                    link = post.previewUrl.replace(to_remove, "");
                    intent = new Intent(getActivity(), ImageActivity.class);
                } else if (postHint.equals("rich:video")) {
                    if (post.domain.contains("youtube.com")) {
                        Uri uri = Uri.parse(post.url);
                        String v = uri.getQueryParameter("v");
                        link = "https://www.youtube.com/embed/" + v;
                        intent = new Intent(getActivity(), LinkActivity.class);
                    } else if (post.domain.contains("youtu.be")) {
                        Uri uri = Uri.parse(post.url);
                        link = "https://www.youtube.com/embed/" + uri.getLastPathSegment();
                        intent = new Intent(getActivity(), LinkActivity.class);
                    } else if (post.domain.contains("gfycat.com")) {
                        String to_remove = "//";
                        link = post.url.replace(to_remove, "//fat.") + ".webm";
                        intent = new Intent(getActivity(), VideoActivity.class);
                    } else {
                        String to_remove = "amp;";
                        link = post.previewUrl.replace(to_remove, "");
                        intent = new Intent(getActivity(), ImageActivity.class);
                    }
                } else if (postHint.equals("image")) {
                    String to_remove = "amp;";
                    link = post.previewUrl.replace(to_remove, "");
                    intent = new Intent(getActivity(), ImageActivity.class);
                } else if (postHint.equals("self") || post.domain.contains("self") && !post.selftext.isEmpty()) {
                    link = post.selftext;
                    intent = new Intent(getActivity(), SelfActivity.class);
                } else if (postHint.isEmpty()) {
                    link = new Gson().toJson(post);
                    intent = new Intent(getActivity(), CommentsActivity.class);
                }

                if (intent != null) {
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
                break;
        }

    }

    public interface OnImageClickListener {
        void onImageSelected(Object movie, View view);
    }
}
