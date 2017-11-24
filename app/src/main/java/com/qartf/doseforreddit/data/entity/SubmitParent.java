package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.JsonAdapter;
import com.qartf.doseforreddit.data.entity.deserializer.SubmitParentDeserializer;

import java.util.List;

@JsonAdapter(SubmitParentDeserializer.class)
public class SubmitParent{

	public boolean success;
	public List<List<String>> jquery;

	public SubmitParent(boolean success, List<List<String>> jquery) {
		this.success = success;
		this.jquery = jquery;
	}
}