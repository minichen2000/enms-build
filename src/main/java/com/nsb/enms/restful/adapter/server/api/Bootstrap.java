package com.nsb.enms.restful.adapter.server.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nsb.enms.restful.db.client.ApiClient;
import com.nsb.enms.restful.db.client.Configuration;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;

public class Bootstrap extends HttpServlet {
	private static final long serialVersionUID = 6213433092343311611L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		Info info = new Info().title("Swagger Server").description("ENMS API.  Adapter RESTful interface. ")
				.termsOfService("").contact(new Contact().email("")).license(new License().name("").url(""));

		ServletContext context = config.getServletContext();
		loadConf(context);

		Swagger swagger = new Swagger().info(info);

		new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
	}

	private void loadConf(ServletContext context) {
		InputStream in = context.getResourceAsStream("/WEB-INF/conf/conf.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String dbUrl = prop.getProperty("DB_URL");
		initDbApiClient(dbUrl);

		String ctrlUrl = prop.getProperty("CTRL_URL");
		initControllerApiClient(ctrlUrl);
	}

	private void initControllerApiClient(String ctrlUrl) {
		com.nsb.enms.restful.controller.client.Configuration
				.setDefaultApiClient(new com.nsb.enms.restful.controller.client.ApiClient().setBasePath(ctrlUrl));
	}

	private void initDbApiClient(String dbUrl) {
		Configuration.setDefaultApiClient(new ApiClient().setBasePath(dbUrl));
	}
}
