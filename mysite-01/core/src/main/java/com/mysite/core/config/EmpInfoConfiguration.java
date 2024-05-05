package com.mysite.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Emp Info Config",description = "Data Featching from Rest ApI Url")
public @interface EmpInfoConfiguration {
	@AttributeDefinition(
			name = "Emp Info Url",
			description = "Rest ApI Url Data",
			defaultValue = "https://gorest.co.in/public/v2/users")
	public String getApiUrl();
	

}
