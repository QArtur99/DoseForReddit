package com.qartf.doseforreddit.presenter.submit;

import com.qartf.doseforreddit.data.entity.RuleParent;

import io.reactivex.Observable;

public interface SubmitMVP {

    interface View{
        void setSubredditRules(RuleParent ruleParent);
        void error(String errorString);
    }

    interface Presenter{
        void setView(SubmitMVP.View view);
        void getSubredditRules();

    }

    interface Model{
        Observable<RuleParent> getSubredditRules();

    }
}
