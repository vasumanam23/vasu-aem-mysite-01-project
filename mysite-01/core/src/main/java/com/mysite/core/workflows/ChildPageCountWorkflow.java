package com.mysite.core.workflows;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mysite.core.services.MysiteUtilService;

@Component(service = WorkflowProcess.class, property = "process.label=Child Page Count Process")
public class ChildPageCountWorkflow implements WorkflowProcess{
	
	@Reference
	MysiteUtilService mysiteUtileService;
	
	ResourceResolver resolver;

	@Override
	public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap) throws WorkflowException {
		String path=workItem.getWorkflowData().getPayload().toString();
		resolver=mysiteUtileService.getResourceResolver();
		Resource resource=resolver.getResource(path);
		int childCount=0;
		if(resource.getResourceType().equals("cq:Page")) {
			PageManager manager=resolver.adaptTo(PageManager.class);
			Page page=manager.getPage(path);
			Iterator<Page> pageIterator=page.listChildren();
			while(pageIterator.hasNext()) {
				childCount++;
				pageIterator.next();
			}
		}
		workItem.getWorkflow().getMetaDataMap().put("child-count",childCount);
	}

}
