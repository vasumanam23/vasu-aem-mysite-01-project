package com.mysite.core.models;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Field {
	@Inject
	private String pathField;
	
	@Inject
	private Calendar dob;

	public String getPathField() {
		return pathField;
	}

	public Calendar getDob() {
		return dob;
	}
	
	@Inject
	public List<Nested> nested;

	public List<Nested> getNested() {
		return nested;
	}

	
	

}
