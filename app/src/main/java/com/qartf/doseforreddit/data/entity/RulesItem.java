package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.SerializedName;

public class RulesItem{

	@SerializedName("violation_reason")
	public String violationReason;

	@SerializedName("kind")
	public String kind;

	@SerializedName("description")
	public String description;

	@SerializedName("short_name")
	public String shortName;

	@SerializedName("description_html")
	public String descriptionHtml;

	@SerializedName("created_utc")
	public double createdUtc;

	@SerializedName("priority")
	public int priority;
}