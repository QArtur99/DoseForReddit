package com.qartf.doseforreddit.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.qartf.doseforreddit.model.deserializer.PostParentDeserialize;

import java.util.List;

@JsonAdapter(PostParentDeserialize.class)
public class PostParent {

    @SerializedName("children")
    public List<Post> postList;
    public String after;
    public String before;

    public PostParent(List<Post> postList, String after, String before) {
        this.postList = postList;
        this.after = after;
        this.before = before;
    }
}
