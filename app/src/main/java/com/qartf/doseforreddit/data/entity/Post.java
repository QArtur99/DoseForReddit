package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.data.entity.deserializer.PostDeserializer;

@JsonAdapter(PostDeserializer.class)
public class Post {

    public String kind;
    public String domain;
    public String subreddit;
    public String selftext;
    public String likes;
    public String linkFlairText;
    public String id;
    public String title;
    public String saved;
    public String previewUrl;
    public String previewGif;
    public String thumbnail;
    public String subRedditId;
    public String postHint;
    public String parentWhiteListStatus;
    public String name;
    public String permaLink;
    public String subRedditType;
    public String url;
    public String whiteListStatus;
    public String author;
    public String createdUTC;
    public String subredditNamePrefixed;
    public String ups;
    public String numComents;


    public Post(String kind, String domain, String subreddit, String selftext, String likes, String linkFlairText, String id, String title, String saved, String previewUrl, String previewGif, String thumbnail, String subRedditId, String postHint, String parentWhiteListStatus, String name, String permaLink, String subRedditType, String url, String whiteListStatus, String author, String createdUTC, String subredditNamePrefixed, String ups, String numComents) {
        this.kind = kind;
        this.domain = domain;
        this.subreddit = subreddit;
        this.selftext = selftext;
        this.likes = likes;
        this.linkFlairText = linkFlairText;
        this.id = id;
        this.title = title;
        this.saved = saved;
        this.previewUrl = previewUrl;
        this.previewGif = previewGif;
        this.thumbnail = thumbnail;
        this.subRedditId = subRedditId;
        this.postHint = postHint;
        this.parentWhiteListStatus = parentWhiteListStatus;
        this.name = name;
        this.permaLink = permaLink;
        this.subRedditType = subRedditType;
        this.url = url;
        this.whiteListStatus = whiteListStatus;
        this.author = author;
        this.createdUTC = createdUTC;
        this.subredditNamePrefixed = subredditNamePrefixed;
        this.ups = ups;
        this.numComents = numComents;
    }
}
