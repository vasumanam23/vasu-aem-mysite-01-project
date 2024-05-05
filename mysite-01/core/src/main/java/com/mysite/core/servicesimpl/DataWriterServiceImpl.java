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
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.core.services.DataReaderService;
import com.mysite.core.services.DataWriterService;
import com.mysite.core.services.ResourceResolverService;

@Component(service = DataWriterService.class,immediate = true)
public class DataWriterServiceImpl implements DataWriterService{
	private static final Logger LOG=LoggerFactory.getLogger(DataWriterServiceImpl.class);
	@Reference
	DataReaderService dataReader;
	
	@Reference
	ResourceResolverService resolverService;
	
	private ResourceResolver resolver;

	@Override
	public void getDataWriter() {
	   JsonArray userData=dataReader.getDataReader();
	   if(userData !=null) {
		   resolver=resolverService.getResourceResolver();
		   Session session=resolver.adaptTo(Session.class);
		   if(session !=null) {
			 try {
				 Node rootNode=session.getRootNode();
				 Node contentNode=rootNode.getNode("content");
				 Node usersNode=null;
				 if(contentNode.hasNode("users")) {
					 usersNode=contentNode.getNode("users");
				 }else {
					 usersNode=contentNode.addNode("users");
				 }
				 createUserNodes(usersNode,userData);
			}catch (RepositoryException e) {
				LOG.error("Error While Writing UserData :",e);
			}
			 try {
				resolver.commit();
			} catch (PersistenceException e) {
				LOG.error("Error While Writing UserData :",e);	
			}
		   }else {
			   LOG.error("Session is null");
		   }
	   }else {
		   LOG.error("User Data is null");
	   }	
	}
	private void createUserNodes(Node usersNode, JsonArray userData) {
		JsonObject user=null;
		for(int i=0; i < userData.size(); i++) {
			user=userData.get(i).getAsJsonObject();
			int id=user.get("id").getAsInt();
			Node userNode=null;
			try {
				if(usersNode.hasNode("user-"+ id)) {
					userNode=usersNode.getNode("user-"+ id);
				}else {
					userNode=usersNode.addNode("user-"+ id);
				}
				userNode.setProperty("id",user.get("id").getAsInt());
				userNode.setProperty("name",user.get("name").getAsString());
				userNode.setProperty("email",user.get("email").getAsString());
				userNode.setProperty("gender",user.get("gender").getAsString());
				userNode.setProperty("status",user.get("status").getAsString());
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
}
