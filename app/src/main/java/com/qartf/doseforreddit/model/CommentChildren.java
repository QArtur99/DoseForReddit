package com.qartf.doseforreddit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ART_F on 2017-09-03.
 */

public class CommentChildren {

    @SerializedName("kind")
    public String kind;

    @SerializedName("subreddit_id")
    public String subredditId;

    @SerializedName("link_id")
    public String linkId;

    @SerializedName("replies")
    public List<CommentChildren> commentChildrenList;

    @SerializedName("author")
    public String author;

    @SerializedName("ups")
    public String ups;

    @SerializedName("parent_id")
    public String parentId;

    @SerializedName("score")
    public String score;

    @SerializedName("body")
    public String body;

    @SerializedName("subreddit_type")
    public String subredditType;

    @SerializedName("name")
    public String name;

    @SerializedName("created_utc")
    public String createdUtc;

    @SerializedName("subreddit_name_prefixed")
    public String subredditNamePrefixed;
}
