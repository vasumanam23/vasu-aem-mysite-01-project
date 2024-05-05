package com.mysite.core.servlets;

import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.apache.sling.servlets.annotations.SlingServletPaths;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
    service = { Servlet.class },
    property = {
        Constants.SERVICE_DESCRIPTION + "=Display Child Page Properties Servlet",
        "sling.servlet.methods=" + "GET",
        "sling.servlet.paths=" + "/bin/displaychildprops"
    }
)
public class DisplayChildPropertiesServlet extends SlingAllMethodsServlet {

    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String pagePath = request.getParameter("/content/users");

        // Get the resource resolver
        ResourceResolver resourceResolver = request.getResourceResolver();

        // Get the resource of the selected page
        Resource pageResource = resourceResolver.getResource(pagePath);

        if (pageResource != null) {
            // Get child pages
            Iterable<Resource> childPages = pageResource.getChildren();

            // Display properties of child pages
            for (Resource childPage : childPages) {
                response.getWriter().println("<h2>" + childPage.getName() + "</h2>");
                response.getWriter().println("<p>");
                response.getWriter().println("Title: " + childPage.getValueMap().get("jcr:title", String.class));
                // Add more properties as needed
                response.getWriter().println("</p>");
            }
        } else {
            response.getWriter().println("Page not found.");
        }
    }
}
