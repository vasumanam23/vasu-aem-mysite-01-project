package com.mysite.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/executeWorkflow")
public class ExecuteWorkflowServlet extends SlingSafeMethodsServlet {
	private static final Logger LOG=LoggerFactory.getLogger(ExecuteWorkflowServlet.class);
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String payloadPath=request.getParameter("path");
		ResourceResolver resolver=request.getResourceResolver();
		WorkflowSession wfSession=resolver.adaptTo(WorkflowSession.class);
		String model="/var/workflow/models/update-referenced-asset";
		WorkflowModel wfModel;
		try {
			wfModel=wfSession.getModel(model);
			WorkflowData wfData=wfSession.newWorkflowData("JCR_PATH", payloadPath);
			wfSession.startWorkflow(wfModel, wfData);
		} catch (WorkflowException e) {
			LOG.error("Error While Starting the Workflow:  ",e);
		}
	}
}
