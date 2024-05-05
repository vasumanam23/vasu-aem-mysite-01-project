package com.mysite.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysite.core.config.ProductButtonConfig;

@Component(
    service = {Servlet.class},
    property = {
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/gorest/data"
    }
)
@Designate(ocd = ProductButtonConfig.class)
@SuppressWarnings("serial")
public class ProductButtonServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ProductButtonServlet.class);
    
    private String goRestUrl;
    
    @Activate
    public void activate(ProductButtonConfig config) {
        this.goRestUrl = config.getGoRestApiUrl();
        LOG.info("Gorest Json data {}", goRestUrl);
    }
    
    @Modified
    public void modified(ProductButtonConfig config) {
        this.goRestUrl = config.getGoRestApiUrl();
        LOG.info("Gorest Json data {}", goRestUrl);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(goRestUrl);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
                JsonParser parser = new JsonParser(); // Create a new JsonParser instance
                JsonElement jsonElement = parser.parse(responseBody);
//                JsonElement jsonElement = JsonParser.parseString(responseBody);

                response.setContentType("application/json");
                if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    response.getWriter().write(jsonArray.toString());
                } else if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    response.getWriter().write(jsonObject.toString());
                }
            } else {
                response.setStatus(statusCode);
                response.getWriter().write("Error occurred: " + httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred: " + e.getMessage());
        } finally {
            httpClient.close();
        }
    }
}
