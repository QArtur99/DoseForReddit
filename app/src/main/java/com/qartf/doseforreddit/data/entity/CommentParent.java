package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.data.entity.deserializer.CommentParentDeserializer;

import java.util.List;

@JsonAdapter(CommentParentDeserializer.class)
public class CommentParent {

    public List<Comment> commentList;

    public CommentParent(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
