package com.qartf.doseforreddit.di;

import com.qartf.doseforreddit.data.network.RetrofitModule;
import com.qartf.doseforreddit.ui.token.BaseNavigationMainActivity;
import com.qartf.doseforreddit.ui.comment.CommentsActivity;
import com.qartf.doseforreddit.ui.token.MainActivity;
import com.qartf.doseforreddit.ui.SplashActivity;
import com.qartf.doseforreddit.ui.submit.SubmitActivity;
import com.qartf.doseforreddit.ui.comment.CommentSettingsDialog;
import com.qartf.doseforreddit.ui.post.PostDetailSettings;
import com.qartf.doseforreddit.ui.comment.DetailFragment;
import com.qartf.doseforreddit.ui.post.PostsFragment;
import com.qartf.doseforreddit.ui.submit.SubmitFragment;
import com.qartf.doseforreddit.ui.subreddit.SubredditsFragment;
import com.qartf.doseforreddit.ui.widget.RedditWidgetService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, TokenModule.class, PostModule.class, RetrofitModule.class,
        SubredditModule.class, SubmitModule.class, SharedPreferencesModule.class, CommentModule.class})
public interface ApplicationComponent {


    void inject(SplashActivity splashActivity);
    void inject(BaseNavigationMainActivity baseNavigationMainActivity);
    void inject(MainActivity mainActivity);
    void inject(CommentsActivity commentsActivity);
    void inject(SubmitActivity submitActivity);
    void inject(PostsFragment postsFragment);
    void inject(SubredditsFragment subredditsFragment);
    void inject(DetailFragment detailFragment);
    void inject(SubmitFragment submitFragment);
    void inject(CommentSettingsDialog commentSettingsDialog);
    void inject(PostDetailSettings postDetailSettings);
    void inject(RedditWidgetService redditWidgetService);


}
