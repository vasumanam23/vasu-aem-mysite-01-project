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

import com.mysite.core.services.ResourceResolverService;

@Component(service=ResourceResolverService.class)
public class ResourceResolverServiceImpl implements ResourceResolverService {
	private static final Logger LOG=LoggerFactory.getLogger(ResourceResolverService.class);
	@Reference
	ResourceResolverFactory resolverFactory;
	
	ResourceResolver resolver;
	
	@Override
	public ResourceResolver getResourceResolver() {
		
		Map<String, Object> serviceUserMap=new HashMap<>();
		serviceUserMap.put(ResourceResolverFactory.SUBSERVICE,"mysite-user");
		try {
			resolver=resolverFactory.getServiceResourceResolver(serviceUserMap);
		} catch (LoginException e) {
			LOG.error("error while getting the resolver using subservice of name {},error:{}","vasu01",e);
		}
		return resolver;
	}

}
