package com.mysite.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/mysite/welcome")
public class WelcomeServlet extends SlingAllMethodsServlet {
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Welcome Servlet in GET method");	
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		ResourceResolver resolver=request.getResourceResolver();
		Resource resource=resolver.getResource("/content/students/");
		String stdId=request.getParameter("stdId");
		Map<String, Object> props=new HashMap<>();
		props.put("firstName", request.getParameter("firstName"));
		props.put("email", request.getParameter("email"));
		props.put("status", request.getParameter("status"));
 		
		resolver.create(resource, stdId, props);
		resolver.commit();
		response.getWriter().write("Student Id created Successfull..."+stdId);
		
	}

}
