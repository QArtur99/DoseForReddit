package com.qartf.doseforreddit.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PostObjectParent {

    public List<PostObject> postObjectList;
    public String after = "";
    public String before = "";

    public PostObjectParent(List<PostObject> postObjectList, JSONObject jsonObject) throws JSONException {
        this.postObjectList = postObjectList;
        if (jsonObject.has("after")) {
            after = jsonObject.getString("after");
        }

        if (jsonObject.has("before")) {
            before = jsonObject.getString("before");
        }
    }

}
