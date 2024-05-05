package com.mysite.core.models;

import java.io.IOException;


import java.io.InputStream;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDropdownModel
{
	@ValueMapValue
	private String jsonUrl;
	
	private Long id;
    private String name;
    private String username;
    private String email;
    private Address address;
    
    private String phone;
    private String website;
    private Company company;

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

	public Address getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getWebsite() {
		return website;
	}

	public Company getCompany() {
		return company;
	}
	
	public String getJsonUrl() {
		return jsonUrl;
	}

	public static class Address
    {
		private String street;
	    private String suite;
	    private String city;
	    private String zipcode;
	    private Geo geo;
		public String getStreet() {
			return street;
		}
		public String getSuite() {
			return suite;
		}
		public String getCity() {
			return city;
		}
		public String getZipcode() {
			return zipcode;
		}
		public Geo getGeo() {
			return geo;
		}
    }
	public static class Geo
    {
        private String lat;
        private String lng;

	    public String getLat() {
			return lat;
		}

		public String getLng() {
			return lng;
		}
    }
	public static class Company
    {
        private String name;
        private String catchPhrase;
        private String bs;

        public String getName() {
            return name;
        }

        public String getCatchPhrase() {
            return catchPhrase;
        }

        public String getBs() {
            return bs;
        }
    }

	@PostConstruct
	protected void init() {
	    String apiUrl = jsonUrl; //Get data from json link to print component use sling model
	    try (InputStream inputStream = new URL(apiUrl).openStream()) {
	        String jsonString = IOUtils.toString(inputStream, "UTF-8");

	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(jsonString);

	        id = root.get("id").asLong();
	        name = root.get("name").asText();
	        username = root.get("username").asText();
	        email = root.get("email").asText();
	        phone = root.get("phone").asText();
	        website = root.get("website").asText();

	        // Address
	        address = new Address();
	        address.street = root.get("address").get("street").asText();
	        address.suite = root.get("address").get("suite").asText();
	        address.city = root.get("address").get("city").asText();
	        address.zipcode = root.get("address").get("zipcode").asText();

	        // Geo
	        address.geo = new Geo();
	        address.geo.lat = root.get("address").get("geo").get("lat").asText();
	        address.geo.lng = root.get("address").get("geo").get("lng").asText();

	        // Company
	        company = new Company();
	        company.name = root.get("company").get("name").asText();
	        company.catchPhrase = root.get("company").get("catchPhrase").asText();
	        company.bs = root.get("company").get("bs").asText();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}