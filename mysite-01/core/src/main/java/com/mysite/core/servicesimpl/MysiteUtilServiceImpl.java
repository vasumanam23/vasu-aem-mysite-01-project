package com.mysite.core.servicesimpl;

import java.util.HashMap;



import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysite.core.services.MysiteUtilService;

@Component(service = MysiteUtilService.class,immediate = true)
public class MysiteUtilServiceImpl implements MysiteUtilService{
	private static final Logger LOG=LoggerFactory.getLogger(MysiteUtilServiceImpl.class);
	
	@Reference
	ResourceResolverFactory factory;
	
	
	ResourceResolver resolver;

	@Override
	public ResourceResolver getResourceResolver() {
		Map<String, Object> props=new HashMap<>();
		props.put(ResourceResolverFactory.SUBSERVICE, "mysite-user");
		try {
			resolver=factory.getServiceResourceResolver(props);
		} catch (LoginException e) {
			LOG.error("error while getting the resolver using subservice of name {},error:{}","mysite",e);
		}
		
		return resolver;
	}

}
