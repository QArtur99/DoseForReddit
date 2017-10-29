package com.qartf.doseforreddit.mvp.data.model;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.mvp.data.model.deserializer.CommentParentDeserializer;

import java.util.List;

@JsonAdapter(CommentParentDeserializer.class)
public class CommentParent {

    public List<Comment> commentList;

    public CommentParent(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
