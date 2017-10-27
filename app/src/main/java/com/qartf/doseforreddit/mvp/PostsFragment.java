package com.qartf.doseforreddit.mvp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.adapter.PostsAdapter;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.mvp.post.PostMVP;
import com.qartf.doseforreddit.mvp.root.App;
import com.qartf.doseforreddit.network.RetrofitControl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class PostsFragment extends BaseFragmentMvp implements PostMVP.View, PostsAdapter.ListItemClickListener {

    ListViewFragmentInterface mCallback;
    private GridLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    Subject<AccessToken> mObservable = PublishSubject.create();

    @Inject
    PostMVP.Presenter presenter;
    @Inject
    Navigator navigator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ((App) context.getApplicationContext()).getComponent().inject(this);
            mCallback = (ListViewFragmentInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        this.navigator.setActivity(getActivity());
        setAdapter(new ArrayList<Post>());
    }

//    public void method() {
//        mObservable.map(new Function<AccessToken, Object>() {
//            @Override
//            public Object apply(@NonNull AccessToken value) throws Exception {
//                presenter.loadPosts();
//                return String.valueOf(value);
//            }
//        }).subscribe();
//    }

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
//        method();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
//        resultList.clear();
//        listAdapter.notifyDataSetChanged();
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
        if (getActivity().findViewById(R.id.detailsViewFrame) != null) {
            mCallback.loadDetailFragment();
            mCallback.setPost(post);
        } else {
//            navigator.startDetailActivity(post, accessToken);
            presenter.loadPosts();
        }
    }

    public interface ListViewFragmentInterface {
        RetrofitControl getRetrofitControl();

        void restoreDetailFragment();

        void loadDetailFragment();

        void setPost(Post post);

        void startDetailActivity(Post post);
    }

}
