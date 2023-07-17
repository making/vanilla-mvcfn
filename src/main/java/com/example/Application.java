package com.example;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

public class Application {

	public static void main(String[] args) throws Exception {
		startTomcat();
	}

	static StaticWebApplicationContext applicationContext(RouterFunction<ServerResponse> routes) {
		StaticWebApplicationContext applicationContext = new StaticWebApplicationContext();
		applicationContext.registerBean(DispatcherServlet.HANDLER_MAPPING_BEAN_NAME,
				HandlerMapping.class, () -> {
					RouterFunctionMapping mapping = new RouterFunctionMapping(routes);
					mapping.setMessageConverters(List.of(
							new StringHttpMessageConverter(),
							new ByteArrayHttpMessageConverter(),
							new AllEncompassingFormHttpMessageConverter(),
							new MappingJackson2HttpMessageConverter()
					));
					return mapping;
				});
		return applicationContext;
	}


	static void startTomcat() throws Exception {
		Tomcat tomcat = new Tomcat();
		int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
		tomcat.getConnector().setPort(port);
		Context context = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
		DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext(Routing.routes()));
		Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet).addMapping("/");
		tomcat.start();
		tomcat.getServer().await();
	}

}
