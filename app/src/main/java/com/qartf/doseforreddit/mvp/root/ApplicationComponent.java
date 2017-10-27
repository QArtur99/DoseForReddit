package com.qartf.doseforreddit.mvp.root;

import com.qartf.doseforreddit.mvp.BaseNavigationMainActivity;
import com.qartf.doseforreddit.mvp.MainActivity;
import com.qartf.doseforreddit.mvp.PostsFragment;
import com.qartf.doseforreddit.mvp.SubredditsFragment;
import com.qartf.doseforreddit.mvp.data.network.RetrofitModule;
import com.qartf.doseforreddit.mvp.post.PostModule;
import com.qartf.doseforreddit.mvp.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.mvp.subreddit.SubredditModule;
import com.qartf.doseforreddit.mvp.token.TokenModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, TokenModule.class, PostModule.class,
        RetrofitModule.class, SubredditModule.class, SharedPreferencesModule.class})
public interface ApplicationComponent {


    void inject(MainActivity mainActivity);
    void inject(PostsFragment postsFragment);
    void inject(SubredditsFragment subredditsFragment);
    void inject(BaseNavigationMainActivity baseNavigationMainActivity);


}
