package com.qartf.doseforreddit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostObjectParent {

    @SerializedName("children")
    public List<PostObject> postObjectList;
    public String after;
    public String before;

}
