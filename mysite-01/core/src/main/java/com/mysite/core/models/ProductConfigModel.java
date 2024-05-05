package com.mysite.core.models;

import java.io.IOException;
import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.core.services.ProductJsonConfigService;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductConfigModel
{
	private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    
	@OSGiService
	ProductJsonConfigService service;
	
	@PostConstruct
	public void init() throws IOException {
        JSONObject jsonData = service.getJsonUrl();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonData.toString());
        
        id = root.get("id").asLong();
        name = root.get("name").asText();
        username = root.get("username").asText();
        email = root.get("email").asText();
        phone = root.get("phone").asText();
        website = root.get("website").asText();
    }
	
	public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}