/**
 * 
 */
package org.sjcdigital.utils;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class ScrapperUtils {
	
	@ConfigProperty(name = "scrapper.agent")
	String agent;
	
	@ConfigProperty(name = "scrapper.timeout")
	int timeout;
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Document getDocument(final String url) throws IOException {
		return getResponse(url).parse();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Response getResponse(final String url) throws IOException {
		
		Response response = Jsoup.connect(url)
								.userAgent(agent)
								.timeout(timeout)
								.method(Method.GET)
								.followRedirects(true)
								.execute();
		return response;
	}
	
	/**
	 * 
	 * @param url
	 * @param parameters
	 * @param cookies
	 * @return
	 * @throws IOException
	 */
	public String getCSVResponse(final String url, Map<String, String> parameters, Map<String, String> cookies) throws IOException {
		
		
		/*
		 * Client client = ClientBuilder.newClient();
		 * 
		 * Form form = new Form(); parameters.forEach((k,v) -> form.param(k, v));
		 * 
		 * Entity<Form> entity = Entity.form(form);
		 * 
		 * return client.target(url) .request("application/CSV") .header("content-type",
		 * "application/x-www-form-urlencoded") .header("cache-control", "no-cache")
		 * .cookie("ASP.NET_SessionId", cookies.get("ASP.NET_SessionId"))
		 * .cookie("__AntiXsrfToken", cookies.get("__AntiXsrfToken"))
		 * .post(Entity.entity(entity, MediaType.APPLICATION_FORM_URLENCODED))
		 * .readEntity(String.class);
		 */
			  
	
		
		Response response = Jsoup.connect(url)
								.userAgent(agent)
								.timeout(timeout)
								.header("content-type", "application/x-www-form-urlencoded")
								.header("cache-control", "no-cache")
								.ignoreContentType(true)
								.data(parameters)
								.cookies(cookies)
								.method(Method.POST)
								.execute();
		return response.body();
	}

}
