package com.qartf.doseforreddit.data.entity.childComment;

import com.google.gson.annotations.SerializedName;
import com.qartf.doseforreddit.data.entity.Comment;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Data{

	@SerializedName("things")
	public List<Comment> commentList;
}