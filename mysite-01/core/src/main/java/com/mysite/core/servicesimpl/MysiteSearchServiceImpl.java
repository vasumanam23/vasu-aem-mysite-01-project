package com.mysite.core.servicesimpl;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.mysite.core.services.MysiteSearchService;
import com.mysite.core.services.ResourceResolverService;

@Component(service = MysiteSearchService.class)
public class MysiteSearchServiceImpl implements MysiteSearchService {
	private static final Logger LOG=LoggerFactory.getLogger(MysiteSearchServiceImpl.class);
	@Reference
	ResourceResolverService resolverService;
	
	@Reference
	QueryBuilder queryBuilder;
	
	ResourceResolver resolver;

	@Override
	public List<String> searchByKeyword(String keyword) {
		resolver=resolverService.getResourceResolver();
		Session session=resolver.adaptTo(Session.class);
		
		List<String> resultPaths=new ArrayList<>();
		
		Map<String, String> predicates=new HashMap<>();
		predicates.put("path", "/content");
		predicates.put("type", "cq:Page");
		predicates.put("fulltext", keyword);
		predicates.put("p.limit", "-1");
		
		Query query=queryBuilder.createQuery(PredicateGroup.create(predicates),session);
		
		SearchResult results=query.getResult();
		for(Hit result:results.getHits()) {
			try {
				resultPaths.add("Title :"+result.getTitle() + "Path :"+result.getPath());
			} catch (RepositoryException e) {
				LOG.error("Error While Getting the Search Result :",e);
			}
		}
		
		return resultPaths;
	}

}
