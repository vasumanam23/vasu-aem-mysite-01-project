package com.mysite.core.models;

import java.util.Calendar;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteInfoModel {
	@Inject
	private String text;
	
	@Inject
	private String country;
	
	@Inject
	private String color;
	@Inject
	private Calendar date;
	
	public Calendar getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	public String getCountry() {
		return country;
	}

	public String getColor() {
		return color;
	}
	
}
