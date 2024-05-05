package com.mysite.core.servlets;

import java.io.IOException;


import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service =  Servlet.class,property = {"sling.servlet.paths=/bin/listnodes",
		Constants.SERVICE_ID + "=List Node Servlet"})
public class ListNodesServlet extends SlingAllMethodsServlet{
	
	private static final Logger LOG=LoggerFactory.getLogger(ListNodesServlet.class);
	ResourceResolver resolver;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String path=request.getParameter("path");
		String nodeType=request.getParameter("nodeType");
		if(StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(nodeType)) {
			resolver=request.getResourceResolver();
			Session session=resolver.adaptTo(Session.class);
			String queryStr="SELECT * FROM [" + nodeType + "] AS s WHERE ISDESCENDANTNODE(s,'" + path + "')";
			Workspace workspace=session.getWorkspace();
			try {
				QueryManager qm=workspace.getQueryManager();
				Query query=qm.createQuery(queryStr, "JCR-SQL2");
				QueryResult queryResult=query.execute();
				RowIterator rowIterator=queryResult.getRows();
				JSONObject jsonObject=new JSONObject();
				int count=0;
				while(rowIterator.hasNext()) {
					Row row=rowIterator.nextRow();
				    Node node=row.getNode();
				    PropertyIterator propertyIterator=node.getProperties();
				    JSONObject propertyJson=new JSONObject();
				    try {
				    	while(propertyIterator.hasNext()) {
				    		Property property=propertyIterator.nextProperty();
				    		if(!property.isMultiple()) {
				    			propertyJson.put(property.getName(), property.getValue());
				    		}
				        } 
				    	jsonObject.put("Node-" + ++count, propertyJson);
				    }catch (JSONException e) {
						LOG.error("Error While Reading Properties from node",e);
					}
				}
				response.getWriter().write(jsonObject.toString());
			} catch (RepositoryException e) {
				LOG.error("Error While Getting the node List..",e);
			}
			
		}else {
			if(StringUtils.isEmpty(path)) {
				response.getWriter().println("Path param is not included in the request.");
			}
			if(StringUtils.isEmpty(nodeType)) {
				response.getWriter().println("Node Type param is not included in the request.");
			}
		}
	}

}
