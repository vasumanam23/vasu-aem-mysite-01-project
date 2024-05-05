package com.mysite.core.servicesimpl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mysite.core.config.EmpInfoConfiguration;
import com.mysite.core.services.EmpInfoReaderService;

@Component(service = EmpInfoReaderServiceImpl.class,immediate = true)
@Designate(ocd = EmpInfoConfiguration.class)
public class EmpInfoReaderServiceImpl implements EmpInfoReaderService {
	private static final Logger LOG=LoggerFactory.getLogger(EmpInfoReaderServiceImpl.class);
	private EmpInfoConfiguration config;

	@Override
	public JsonArray getEmpInfoReader() {
		String url=config.getApiUrl();
		HttpClient client=HttpClientBuilder.create().build();
		HttpGet get=new HttpGet(url);
		JsonArray empDetails=null;
		try {
			HttpResponse response=client.execute(get);
			HttpEntity entity=response.getEntity();
			String empStr=EntityUtils.toString(entity);
			empDetails=new Gson().fromJson(empStr, JsonArray.class);
		}catch (ClientProtocolException e) {
			LOG.error("Error While Writing User API ");
			
		}catch (IOException e) {
            LOG.error("Error While Writing User API ", e);
		}
		return empDetails;
	}
	@Activate
	public void activate(EmpInfoConfiguration config) {
		this.config=config;
		
	}
	
}
