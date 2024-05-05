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

import com.mysite.core.services.DataWriterService;

@Component(service=Servlet.class)
@SlingServletPaths(value="/bin/userdetails")
public class ReaderServlet extends SlingSafeMethodsServlet{
	
	
	@Reference
	DataWriterService writerService;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		writerService.getDataWriter();
		
		response.getWriter().write("Data written successfull..... ");
	}

}
