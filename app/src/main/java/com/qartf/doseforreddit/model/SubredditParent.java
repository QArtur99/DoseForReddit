package com.qartf.doseforreddit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ART_F on 2017-09-05.
 */

public class SubredditParent {

    @SerializedName("children")
    public List<Subreddit> subredditList;
    public String after;
    public String before;

}
