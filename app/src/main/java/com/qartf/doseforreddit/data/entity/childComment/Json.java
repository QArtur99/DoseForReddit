package com.qartf.doseforreddit.data.entity.childComment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Json{

	@SerializedName("data")
	public Data data;

	@SerializedName("errors")
	public List<Object> errors;
}