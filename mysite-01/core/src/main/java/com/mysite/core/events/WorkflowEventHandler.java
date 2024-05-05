package com.mysite.core.events;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.mysite.core.services.MysiteUtilService;

@Component(immediate = true, service=EventHandler.class)
public class WorkflowEventHandler implements EventHandler{
	private static final Logger LOG=LoggerFactory.getLogger(WorkflowEventHandler.class);
	@Reference
	MysiteUtilService utilService;

	@Override
	public void handleEvent(Event event) {
		String eventType=event.getProperty("type").toString();
		String payloadPath=(String)event.getProperty("assetPath");
		if(StringUtils.isNotEmpty(payloadPath) && payloadPath.startsWith("/content/dam/mysite")
				&& StringUtils.isNotEmpty(eventType) && eventType.equals("ASSET_CREATED")) {
			ResourceResolver resolver=utilService.getResourceResolver();
			WorkflowSession wfSession=resolver.adaptTo(WorkflowSession.class);
			String model="/var/workflow/models/update-referenced-asset";
			try {
				WorkflowModel wfModel=wfSession.getModel(model);
				WorkflowData wfData=wfSession.newWorkflowData("JCR_PATH", payloadPath);
				wfSession.startWorkflow(wfModel, wfData);
			} catch (WorkflowException e) {
				LOG.error("Error while starting the workflow using event handler, ",e );
			}
		}
	}

}
