package com.mysite.core.servicesimpl;

import java.util.Map;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.mysite.core.services.ReferencedAssetService;
import com.mysite.core.services.ResourceResolverService;
@Component(service = ReferencedAssetService.class)
public class ReferencedAssetServiceImpl implements ReferencedAssetService{
	@Reference
	ResourceResolverService resolverService;
	
	ResourceResolver resolver;

	@Override
	public Map<String, Asset> getReferencedAsset(String pagePath) {
		resolver=resolverService.getResourceResolver();
		//Session session=resolver.adaptTo(Session.class);
		Resource resource=resolver.getResource(pagePath);
		//Node node=session.getNode(pagePath);
		Node node=resource.adaptTo(Node.class);
		AssetReferenceSearch assetReferenceSearch=new AssetReferenceSearch(node, DamConstants.MOUNTPOINT_ASSETS, resolver);
		Map<String, Asset> referencedAsset=assetReferenceSearch.search(); 
		
		return referencedAsset;
	}

	

}
