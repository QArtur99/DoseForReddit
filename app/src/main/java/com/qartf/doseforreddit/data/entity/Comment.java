package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.data.entity.deserializer.CommentDeserializer;

import java.util.List;

@JsonAdapter(CommentDeserializer.class)
public class Comment {


    public String kind;
    public String subredditId;
    public String linkId;
    public List<Comment> commentList;
    public String author;
    public String ups;
    public String parentId;
    public String score;
    public String body;
    public String subredditType;
    public String name;
    public String depth;
    public String createdUtc;
    public String subredditNamePrefixed;
    public List<String> childs;

    public Comment(String kind, String subredditId, String linkId, List<Comment> commentList, String author, String ups, String parentId, String score, String body, String subredditType, String name, String depth, String createdUtc, String subredditNamePrefixed) {
        this.kind = kind;
        this.subredditId = subredditId;
        this.linkId = linkId;
        this.commentList = commentList;
        this.author = author;
        this.ups = ups;
        this.parentId = parentId;
        this.score = score;
        this.body = body;
        this.subredditType = subredditType;
        this.name = name;
        this.depth = depth;
        this.createdUtc = createdUtc;
        this.subredditNamePrefixed = subredditNamePrefixed;
    }

    public Comment(String kind, String parentId, String name, List<String> childs){
        this.kind = kind;
        this.parentId = parentId;
        this.name = name;
        this.childs = childs;
    }
}
