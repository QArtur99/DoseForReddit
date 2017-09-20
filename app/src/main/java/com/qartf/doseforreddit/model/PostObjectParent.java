package com.qartf.doseforreddit.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.qartf.doseforreddit.model.deserializer.PostObjectParentDeserialize;

import java.util.List;

@JsonAdapter(PostObjectParentDeserialize.class)
public class PostObjectParent {

    @SerializedName("children")
    public List<PostObject> postObjectList;
    public String after;
    public String before;

    public PostObjectParent(List<PostObject> postObjectList, String after, String before) {
        this.postObjectList = postObjectList;
        this.after = after;
        this.before = before;
    }
}
