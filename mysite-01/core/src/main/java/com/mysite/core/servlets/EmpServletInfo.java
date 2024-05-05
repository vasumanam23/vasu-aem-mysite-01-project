package com.mysite.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mysite.core.services.EmpInfoWriterService;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/emps/generation")
public class EmpServletInfo extends SlingSafeMethodsServlet{
	@Reference
	EmpInfoWriterService empWriter;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		empWriter.getEmpInfoWriter();
		response.getWriter().write("Successfully generated emps node and empid nodes...");
		
	}

}
