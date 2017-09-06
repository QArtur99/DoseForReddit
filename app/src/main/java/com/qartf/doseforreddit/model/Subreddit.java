package com.qartf.doseforreddit.model;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.model.deserializer.SubredditDeserializer;

@JsonAdapter(SubredditDeserializer.class)
public class Subreddit {

    public String kind;
    public String banner_img;
    public String user_is_banned;
    public String wiki_enabled;
    public String show_media;
    public String id;
    public String display_name_prefixed;
    public String submit_text;
    public String display_name;
    public String title;
    public String user_has_favorited;
    public String over18;
    public String description;
    public String user_is_muted;
    public String submit_link_label;
    public String subscribers;
    public String submit_text_label;
    public String whitelist_status;
    public String key_color;
    public String name;
    public String url;
    public String created_utc;
    public String user_is_contributor;
    public String subreddit_type;
    public String user_is_subscriber;

    public Subreddit(String kind, String banner_img, String user_is_banned, String wiki_enabled, String show_media, String id, String display_name_prefixed, String submit_text, String display_name, String title, String user_has_favorited, String over18, String description, String user_is_muted, String submit_link_label, String subscribers, String submit_text_label, String whitelist_status, String key_color, String name, String url, String created_utc, String user_is_contributor, String subreddit_type, String user_is_subscriber) {
        this.kind = kind;
        this.banner_img = banner_img;
        this.user_is_banned = user_is_banned;
        this.wiki_enabled = wiki_enabled;
        this.show_media = show_media;
        this.id = id;
        this.display_name_prefixed = display_name_prefixed;
        this.submit_text = submit_text;
        this.display_name = display_name;
        this.title = title;
        this.user_has_favorited = user_has_favorited;
        this.over18 = over18;
        this.description = description;
        this.user_is_muted = user_is_muted;
        this.submit_link_label = submit_link_label;
        this.subscribers = subscribers;
        this.submit_text_label = submit_text_label;
        this.whitelist_status = whitelist_status;
        this.key_color = key_color;
        this.name = name;
        this.url = url;
        this.created_utc = created_utc;
        this.user_is_contributor = user_is_contributor;
        this.subreddit_type = subreddit_type;
        this.user_is_subscriber = user_is_subscriber;
    }
}
