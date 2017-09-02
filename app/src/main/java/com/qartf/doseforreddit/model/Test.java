package com.qartf.doseforreddit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ART_F on 2017-09-02.
 */

public class Test {

    @SerializedName("children")
    public List<TestObject> postObjectList;
    public String after;
    public String before;
}
