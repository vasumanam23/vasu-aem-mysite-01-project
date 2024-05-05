package com.mysite.core.workflows;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.mysite.core.services.ReferencedAssetService;
import com.mysite.core.services.ResourceResolverService;

@Component(service = WorkflowProcess.class,property = {"process.label=Update Referenced Assets"})
public class UpdateReferencedAssetWorkflow implements WorkflowProcess {
	
	private static final Logger LOG=LoggerFactory.getLogger(UpdateReferencedAssetWorkflow.class);
	
	@Reference
	ReferencedAssetService referencedAssetService;
	
	@Reference
	ResourceResolverService resolverService;
	
	ResourceResolver resolver;

	@Override
	public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap) throws WorkflowException {
		String path=workItem.getWorkflowData().getPayload().toString();
		resolver=resolverService.getResourceResolver();

		if(StringUtils.isNotEmpty(path)) {
			Map<String, Asset> referencedAsset=referencedAssetService.getReferencedAsset(path);
			String[]  processArgs=metaDataMap.get("PROCESS_ARGS","default").split("=");
			for(Map.Entry<String, Asset> entry:referencedAsset.entrySet()) {
				String assetPath=entry.getKey();
				Resource resource=resolver.getResource(assetPath);
				Resource metaDataResource=resource.getChild("jcr:content/metadata");
				ModifiableValueMap mvm=metaDataResource.adaptTo(ModifiableValueMap.class);
				mvm.put(processArgs[0], processArgs[1]);
				try {
					resolver.commit();
				} catch (PersistenceException e) {
					LOG.error("Error While updating the metadata from workflow: ",e);
				}
				
			}
		}
		
	}

}
