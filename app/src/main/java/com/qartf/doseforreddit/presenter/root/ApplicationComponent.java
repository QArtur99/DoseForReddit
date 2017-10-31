package com.qartf.doseforreddit.presenter.root;

import com.qartf.doseforreddit.data.network.RetrofitModule;
import com.qartf.doseforreddit.presenter.comment.CommentModule;
import com.qartf.doseforreddit.presenter.post.PostModule;
import com.qartf.doseforreddit.presenter.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.presenter.subreddit.SubredditModule;
import com.qartf.doseforreddit.presenter.token.TokenModule;
import com.qartf.doseforreddit.view.activity.BaseNavigationMainActivity;
import com.qartf.doseforreddit.view.activity.CommentsActivity;
import com.qartf.doseforreddit.view.activity.MainActivity;
import com.qartf.doseforreddit.view.fragment.DetailFragment;
import com.qartf.doseforreddit.view.fragment.PostsFragment;
import com.qartf.doseforreddit.view.fragment.SubredditsFragment;
import com.qartf.doseforreddit.view.widget.GridRemoteViewsFactory;
import com.qartf.doseforreddit.view.widget.RedditWidgetService;

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
    void inject(RedditWidgetService redditWidgetService);


}