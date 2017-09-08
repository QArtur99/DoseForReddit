package com.qartf.doseforreddit.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
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


public class ListViewFragment extends Fragment implements PostsAdapter.ListItemClickListener,
        SwipyRefreshLayout.OnRefreshListener, View.OnTouchListener {

    @BindView(R.id.loading_indicator) public ProgressBar loadingIndicator;
    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.empty_title_text) TextView emptyTitleText;
    @BindView(R.id.empty_subtitle_text) TextView emptySubtitleText;

    private MainActivity mainActivity;
    private GridLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private String sortBy;
    private OnImageClickListener mCallback;
    private int loaderId;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private int firstView = 0;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, rootView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recyclerView.setOnTouchListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        emptyView.setVisibility(View.GONE);
        mainActivity = ((MainActivity) getActivity());
        setAdapter(new ArrayList<PostObject>());
        bundle = getArguments();
        getBundleData();

        return rootView;
    }

    private void getBundleData() {
        if (bundle != null) {
            firstView = sharedPreferences.getInt(getString(R.string.pref_firstView), 0);
        }
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


    public void onLoadFinished(Loader loader, PostObjectParent postObjectParent) {
        loaderId = loader.getId();
        if (postObjectParent != null) {
            List<PostObject> data = postObjectParent.postObjectList;
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
                emptyView.setVisibility(View.VISIBLE);
                emptyTitleText.setText(getString(R.string.server_problem));
                emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
            } else {
                setInfoNoConnection();
            }
        }
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void setInfoNoConnection() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.no_connection));
        emptySubtitleText.setText(getString(R.string.no_connection_sub_text));
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
        PostObject post = (PostObject) postsAdapter.getDataAtPosition(clickedItemIndex);
        switch (view.getId()) {
            case R.id.imageContainer:
                Utility.startIntentPreview(getActivity(), post);
                break;
            case R.id.commentsAction:
                sharedPreferences.edit().putInt(getString(R.string.pref_lastClicked), clickedItemIndex).apply();
                mCallback.onImageSelected(post, view);
                break;
            default:
                mCallback.onImageSelected(post, view);
        }

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mCallback.onRefresh(loaderId);
        swipyRefreshLayout.setRefreshing(false);
    }

    public interface OnImageClickListener {
        void restoreDetailFragment();

        void onRefresh(int loaderId);

        void onImageSelected(Object movie, View view);
    }
}
