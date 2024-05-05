package com.mysite.core.models;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import com.google.gson.Gson;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataFecherModel {

	@SlingObject
	private Resource resource;

	private String id;
	private String name;
	private String username;
	private String email;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return username;
	}

	public String getEmailId() {
		return email;
	}

	@PostConstruct
	public void init() {
		String apiUrl = "https://jsonplaceholder.typicode.com/users/1";
		fetchExternalData(apiUrl);
	}

	private void fetchExternalData(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed :HTTP Error code:" + conn.getResponseCode());
			}
			Gson gson = new Gson();
			ExternalData data = gson.fromJson(new InputStreamReader(conn.getInputStream()), ExternalData.class);
			this.id = data.getId();
			this.name = data.getName();
			this.username = data.getUserName();
			this.email = data.getEmail();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ExternalData {
		private String id;
		private String name;
		private String username;
		private String email;

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getUserName() {
			return username;
		}

		public String getEmail() {
			return email;
		}
	}
}