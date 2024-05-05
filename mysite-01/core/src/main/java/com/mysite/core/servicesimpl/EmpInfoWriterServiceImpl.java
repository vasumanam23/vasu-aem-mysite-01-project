package com.mysite.core.servicesimpl;

import javax.jcr.ItemExistsException;



import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.core.services.EmpInfoReaderService;
import com.mysite.core.services.EmpInfoWriterService;
import com.mysite.core.services.ResourceResolverService;

@Component(service = EmpInfoWriterService.class,  immediate = true)
public class EmpInfoWriterServiceImpl implements EmpInfoWriterService {
	private static final Logger LOG=LoggerFactory.getLogger(EmpInfoWriterServiceImpl.class);
	
	@Reference
	ResourceResolverService resolverService;
	
	@Reference
	EmpInfoReaderService empReader;
	
	private ResourceResolver resolver;

	@Override
	public void getEmpInfoWriter() {
		JsonArray empDetails=empReader.getEmpInfoReader();
		
		if(empDetails !=null) {
			resolver=resolverService.getResourceResolver();
			Session session=resolver.adaptTo(Session.class);
			if(session !=null) {
				try {
					 Node rootNode=session.getRootNode();
					 Node contentNode=rootNode.getNode("content");
					 Node empsNode=null;
					 if(contentNode.hasNode("emps")) {
						 empsNode=contentNode.getNode("emps");
					 }else {
						 empsNode=contentNode.addNode("emps");
					 }
					 createEmpNodes(empsNode,empDetails);
				}catch (RepositoryException e) {
					LOG.error("Error While Writing UserData :",e);
				}
				 try {
					resolver.commit();
				} catch (PersistenceException e) {
					LOG.error("Error While Writing UserData :",e);	
				}
				
			}else {
				LOG.error("Session is null..");
			}
			
		}else {
			LOG.info("empDetails is null");
		}
		
	}
	private void createEmpNodes(Node empsNode, JsonArray empDetails) {
		JsonObject emp=null;
		for(int i=0; i < empDetails.size(); i++) {
			emp=empDetails.get(i).getAsJsonObject();
			int id=emp.get("id").getAsInt();
			Node empNode=null;
			try {
				if(empsNode.hasNode("emp-"+ id)) {
					empNode=empsNode.getNode("emp-"+ id);
				}else {
					empNode=empsNode.addNode("emp-"+ id);
				}
				empNode.setProperty("id",emp.get("id").getAsInt());
				empNode.setProperty("name",emp.get("name").getAsString());
				empNode.setProperty("email",emp.get("email").getAsString());
				empNode.setProperty("gender",emp.get("gender").getAsString());
				empNode.setProperty("status",emp.get("status").getAsString());
			} catch (PathNotFoundException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			} catch (ItemExistsException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			} catch (VersionException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			} catch (ConstraintViolationException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			} catch (LockException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			} catch (RepositoryException e) {
				LOG.error("error while creating user node with id {},error: {} ", id,e);
			}
		}
			
	}
	@Activate
	public void activate() {
		getEmpInfoWriter();	
	}
	
}
