package com.mysite.core.servicesimpl;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysite.core.config.ProductJsonConfig;
import com.mysite.core.services.ProductJsonConfigService;

@Component(service = ProductJsonConfigService.class,immediate = true )
@Designate(ocd = ProductJsonConfig.class)
public class ProductJsonConfigServiceImpl implements ProductJsonConfigService {
	private static final Logger LOG=LoggerFactory.getLogger(ProductJsonConfigServiceImpl.class);
	
	private String jsonApiUrl;
	
	@Override
	public JSONObject getJsonUrl() {
		JSONObject jsonData=null;
		 try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	            HttpGet request = new HttpGet(jsonApiUrl);
	            HttpResponse response = httpClient.execute(request);
	            String jsonString = EntityUtils.toString(response.getEntity());
	            JSONObject json = new JSONObject(jsonString);
	            jsonData = json;
	            LOG.info("JsonData "+jsonData);
	        } catch (IOException  e) { 
	            LOG.error("Errror while fetching the data",e);
	        } catch (JSONException e) {
	        	LOG.error("Errror while fetching the data",e);
			}
		
		return jsonData;
	}
	
	@Activate
	public void activate(ProductJsonConfig config) {
		this.jsonApiUrl=config.jsonUrl();
		LOG.info("JsonApi url {}",jsonApiUrl);
		getJsonUrl();
	}
	
	@Modified
	public void modified(ProductJsonConfig config) {
		this.jsonApiUrl=config.jsonUrl();
		LOG.info("Json Api Url {}",jsonApiUrl);
		getJsonUrl();
	}
	
}