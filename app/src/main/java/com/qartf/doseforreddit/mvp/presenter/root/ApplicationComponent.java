package com.qartf.doseforreddit.mvp.presenter.root;

import com.qartf.doseforreddit.mvp.data.network.RetrofitModule;
import com.qartf.doseforreddit.mvp.presenter.comment.CommentModule;
import com.qartf.doseforreddit.mvp.presenter.post.PostModule;
import com.qartf.doseforreddit.mvp.presenter.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.mvp.presenter.subreddit.SubredditModule;
import com.qartf.doseforreddit.mvp.presenter.token.TokenModule;
import com.qartf.doseforreddit.mvp.view.activity.BaseNavigationMainActivity;
import com.qartf.doseforreddit.mvp.view.activity.CommentsActivity;
import com.qartf.doseforreddit.mvp.view.activity.MainActivity;
import com.qartf.doseforreddit.mvp.view.fragment.DetailFragment;
import com.qartf.doseforreddit.mvp.view.fragment.PostsFragment;
import com.qartf.doseforreddit.mvp.view.fragment.SubredditsFragment;
import com.qartf.doseforreddit.mvp.view.widget.GridRemoteViewsFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, TokenModule.class, PostModule.class, RetrofitModule.class,
        SubredditModule.class, SharedPreferencesModule.class, CommentModule.class})
public interface ApplicationComponent {


//    void inject(BaseActivity baseActivity);
    void inject(BaseNavigationMainActivity baseNavigationMainActivity);
    void inject(MainActivity mainActivity);
    void inject(CommentsActivity commentsActivity);
    void inject(PostsFragment postsFragment);
    void inject(SubredditsFragment subredditsFragment);
    void inject(DetailFragment detailFragment);
    void inject(GridRemoteViewsFactory gridRemoteViewsFactory);


}
