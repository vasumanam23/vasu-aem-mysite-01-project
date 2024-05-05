package com.mysite.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="Product Button Config",description = "Fetching JsonData form Rest Api Url")
public @interface ProductButtonConfig {
	
	@AttributeDefinition(name="Product Button",description = "Featch Data from rest api url")
	String getGoRestApiUrl() default"https://gorest.co.in/public/v2/users";
	
}
