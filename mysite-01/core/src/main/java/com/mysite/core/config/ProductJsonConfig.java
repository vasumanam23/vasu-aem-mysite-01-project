package com.mysite.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "ProductJsonRestApi Service", description = "Service to provide configuration values.")
public @interface ProductJsonConfig {
	
	@AttributeDefinition(name="ProductJsonApi Url",description = "Featch Data from rest api url")
	String jsonUrl() default"https://jsonplaceholder.typicode.com/users/1";
	
}
