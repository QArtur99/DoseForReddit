package com.qartf.doseforreddit.ui.submit;

import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;

import java.util.HashMap;

import io.reactivex.Observable;

public interface SubmitMVP {

    interface View{
        void setPost(Post post);
        void setSubredditRules(RuleParent ruleParent);
        void setSubmitted(String url);
        void error(String errorString);
    }

    interface Presenter{
        void setView(SubmitMVP.View view);
        void getSubredditRules();
        void postSubmit(HashMap<String, String> args);
        void getPost(String url);

    }

    interface Model{
        boolean checkConnection();
        Observable<RuleParent> getSubredditRules();
        Observable<SubmitParent> postSubmit(HashMap<String, String> args);
        Observable<PostParent> getPost(String url);

    }
}
