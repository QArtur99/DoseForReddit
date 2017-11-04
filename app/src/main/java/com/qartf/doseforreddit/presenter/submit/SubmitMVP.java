package com.qartf.doseforreddit.presenter.submit;

import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;

import java.util.HashMap;

import io.reactivex.Observable;

public interface SubmitMVP {

    interface View{
        void setSubredditRules(RuleParent ruleParent);
        void setSubmitted();
        void error(String errorString);
    }

    interface Presenter{
        void setView(SubmitMVP.View view);
        void getSubredditRules();
        void postSubmit(HashMap<String, String> args);

    }

    interface Model{
        boolean checkConnection();
        Observable<RuleParent> getSubredditRules();
        Observable<SubmitParent> postSubmit(HashMap<String, String> args);

    }
}
