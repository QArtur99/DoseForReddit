package com.qartf.doseforreddit.fragment;

import android.content.Context;
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

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.adapter.PostsAdapter;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListViewFragment extends Fragment implements PostsAdapter.ListItemClickListener {

    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.loading_indicator) public ProgressBar loadingIndicator;
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
        PostObject post = (PostObject) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.imageContainer:
                Utility.startIntentPreview(getActivity(), post);
                break;
            default:
                mCallback.onImageSelected(post, view);
        }

    }

    public interface OnImageClickListener {
        void onImageSelected(Object movie, View view);
    }
}
