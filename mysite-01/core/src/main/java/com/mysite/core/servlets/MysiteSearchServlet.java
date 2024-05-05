package com.mysite.core.servlets;

import java.io.IOException;



import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mysite.core.services.MysiteSearchService;

@Component(service =  Servlet.class,
property = {"sling.servlet.paths=/bin/mysite/search",
		Constants.SERVICE_ID+ "=My Site Search Servlet",
		"sling.servlet.methods=GET"})
public class MysiteSearchServlet extends SlingSafeMethodsServlet {
	
	@Reference
	MysiteSearchService searchService;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String keyword=request.getParameter("keyword");
		List<String> resultPaths=searchService.searchByKeyword(keyword);
		StringBuilder sb=new StringBuilder();
		for(String path:resultPaths) {
			sb.append(path).append("\n");
		}
		response.getWriter().write(sb.toString());
	}

}
