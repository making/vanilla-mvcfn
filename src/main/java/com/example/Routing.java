package com.example;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

public class Routing {
	public static RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.GET("/", request -> ServerResponse.ok().body("Hello World!"))
				.build();
	}
}
