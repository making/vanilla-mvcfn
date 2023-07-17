package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routing {

	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.GET("/", request -> ServerResponse.ok().body("Hello World!"))
				.build();
	}
}