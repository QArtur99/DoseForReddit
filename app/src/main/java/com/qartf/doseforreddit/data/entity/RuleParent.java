package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RuleParent{

	@SerializedName("site_rules")
	public List<String> siteRules;

	@SerializedName("rules")
	public List<RulesItem> rules;
}