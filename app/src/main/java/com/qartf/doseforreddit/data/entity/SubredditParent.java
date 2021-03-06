package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.qartf.doseforreddit.data.entity.deserializer.SubredditParentDeserializer;

import java.util.List;


@JsonAdapter(SubredditParentDeserializer.class)
public class SubredditParent {

    @SerializedName("children")
    public List<Subreddit> subredditList;
    public String after;
    public String before;

    public SubredditParent(List<Subreddit> subredditList, String after, String before) {
        this.subredditList = subredditList;
        this.after = after;
        this.before = before;
    }
}
