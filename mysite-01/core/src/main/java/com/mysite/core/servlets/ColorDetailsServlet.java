package com.mysite.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.commons.collections4.Transformer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONArray;
import org.json.JSONException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;

@Component(service =  Servlet.class)
@SlingServletResourceTypes(resourceTypes = "/apps/color/details")
public class ColorDetailsServlet extends SlingSafeMethodsServlet{
	private static final Logger LOG=LoggerFactory.getLogger(ColorDetailsServlet.class);
	ResourceResolver resolver;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		resolver=request.getResourceResolver();
		Resource resource=resolver.getResource("/content/dam/mysite01/color.json");
		
		Asset asset=DamUtil.resolveToAsset(resource);
		Rendition originalRendition=asset.getOriginal();
		
		InputStream is=originalRendition.adaptTo(InputStream.class);
		
		StringBuilder sb=new StringBuilder();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String line;
		
		while(null != (line=br.readLine())) {
			sb.append(line);
		}
		LOG.info("Color Json Date :"+sb.toString());
		
		try {
			JSONArray jsonContent=new JSONArray(sb.toString());
			Map<String, String> data=new TreeMap<>();
			for(int i=0; i < jsonContent.length(); i++) {
				data.put(jsonContent.getJSONObject(i).getString("text"),
						jsonContent.getJSONObject(i).getString("value"));
			}
			
		} catch (JSONException e) {
			LOG.error("Error while getting the JsonArry ",e);
		}
		
		
		
	}

}
