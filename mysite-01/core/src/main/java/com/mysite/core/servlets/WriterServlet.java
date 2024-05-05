package com.mysite.core.servlets;

import java.io.IOException;


import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.core.services.DataReaderService;

@Component(service = Servlet.class)
@SlingServletPaths(value="/bin/user/writer")
public class WriterServlet extends SlingSafeMethodsServlet {
	@Reference
	DataReaderService reader;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		 JsonArray allUsers=reader.getDataReader();
		 
		 String userId=request.getParameter("id");
		 String status=request.getParameter("status");
		 
		 response.setContentType("application/json");
		 JsonObject user=null;
		 if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(status)) {
			 for(int i=0; i < allUsers.size(); i++){
				JsonObject currentUser=allUsers.get(i).getAsJsonObject();
				if(userId.equals(currentUser.get("id").getAsString()) && 
						status.equals(currentUser.get("status").getAsString())) {
					user=currentUser;
				}
			 }
			 if(user !=null) {
				 response.getWriter().write(user.toString());
			 }else {
				 response.getWriter().write("No user available with the details given");
			 }
		 }else {
			 response.getWriter().write("Required params are not fully provided...");
		 }	 	
	}

}
