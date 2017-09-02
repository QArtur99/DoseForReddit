package com.qartf.doseforreddit.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostObject {

    public  String kind = "";
    public  String domain = "";
    public  String subreddit = "";
    public  String selftext = "";
    public  String linkFlairText = "";
    public  String id = "";
    public  String title = "";
    public  String previewUrl = "";
    public  String previewGif = "";
    public  String thumbnail = "";
    public  String subRedditId = "";
    public  String postHint = "";
    public  String parentWhiteListStatus = "";
    public  String name = "";
    public  String permaLink = "";
    public  String subRedditType = "";
    public  String url = "";
    public  String whiteListStatus = "";
    public  String author = "";
    public  String createdUTC = "";
    public  String subredditNamePrefixed = "";
    public  String ups = "";
    public  String numComents = "";

    public PostObject(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("kind")) {
            this.kind = jsonObject.getString("kind");
        }
        setData(jsonObject.getJSONObject("data"));
    }

    private void setData(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("domain")) {
            domain = jsonObject.getString("domain");
        }
        if (jsonObject.has("subreddit")) {
            subreddit = jsonObject.getString("subreddit");
        }
        if (jsonObject.has("selftext")) {
            selftext = jsonObject.getString("selftext");
        }
        if (jsonObject.has("link_flair_text")) {
            linkFlairText = jsonObject.getString("link_flair_text");
        }
        if (jsonObject.has("id")) {
            id = jsonObject.getString("id");
        }
        if (jsonObject.has("title")) {
            title = jsonObject.getString("title");
        }

        if (jsonObject.has("preview")) {
            JSONArray images = jsonObject.getJSONObject("preview").getJSONArray("images");
            JSONObject source = images.getJSONObject(0);
            if (source.has("source")) {
                previewUrl = source.getJSONObject("source").getString("url");
            }
            if (source.has("variants")) {
                JSONObject variants = source.getJSONObject("variants");
                if (variants.has("gif")) {
                    previewGif = variants.getJSONObject("mp4").getJSONObject("source").getString("url");
                }
            }
        }

        if (jsonObject.has("thumbnail")) {
            thumbnail = jsonObject.getString("thumbnail");
        }
        if (jsonObject.has("subreddit_id")) {
            subRedditId = jsonObject.getString("subreddit_id");
        }
        if (jsonObject.has("post_hint")) {
            postHint = jsonObject.getString("post_hint");
        }
        if (jsonObject.has("parent_whitelist_status")) {
            parentWhiteListStatus = jsonObject.getString("parent_whitelist_status");
        }
        if (jsonObject.has("name")) {
            name = jsonObject.getString("name");
        }
        if (jsonObject.has("permalink")) {
            permaLink = jsonObject.getString("permalink");
        }
        if (jsonObject.has("subreddit_type")) {
            subRedditType = jsonObject.getString("subreddit_type");
        }
        if (jsonObject.has("url")) {
            url = jsonObject.getString("url");
        }
        if (jsonObject.has("whitelist_status")) {
            whiteListStatus = jsonObject.getString("whitelist_status");
        }
        if (jsonObject.has("author")) {
            author = jsonObject.getString("author");
        }
        if (jsonObject.has("created_utc")) {
            createdUTC = jsonObject.getString("created_utc");
        }
        if (jsonObject.has("subreddit_name_prefixed")) {
            subredditNamePrefixed = jsonObject.getString("subreddit_name_prefixed");
        }
        if (jsonObject.has("ups")) {
            ups = jsonObject.getString("ups");
        }
        if (jsonObject.has("num_comments")) {
            numComents = jsonObject.getString("num_comments");

        }
    }
}
