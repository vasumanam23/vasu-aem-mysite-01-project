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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.core.services.EmpInfoReaderService;

@Component(service =  Servlet.class)
@SlingServletPaths(value = "/bin/emps/details")
public class EmpServletParams extends SlingSafeMethodsServlet{
	private static final Logger LOG=LoggerFactory.getLogger(EmpServletParams.class);
	
	@Reference
	EmpInfoReaderService empReader;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
      JsonArray	empDetails=empReader.getEmpInfoReader();
      
      String empId=request.getParameter("id");
      String status=request.getParameter("status");
      
      response.setContentType("application/json");
      JsonObject emp=null;
      if(StringUtils.isNotEmpty(empId) && StringUtils.isNotEmpty(status)) {
    	  for(int i=0; i < empDetails.size(); i++) {
    		  JsonObject currentEmp=empDetails.get(i).getAsJsonObject();
    		  if(empId.equals(currentEmp.get("id").getAsString()) &&
    				  status.equals(currentEmp.get("status").getAsString())) {
    			  emp=currentEmp;
    		  }
    	  }
    	  if(emp !=null) {
    		  response.getWriter().write(emp.toString());
    	  }else {
    		  response.getWriter().write("No Employee available with the details given");
    	  }
    	  
      }else {
    	  response.getWriter().write("Required Parameters are not fully Provided...");      }
	}

}
