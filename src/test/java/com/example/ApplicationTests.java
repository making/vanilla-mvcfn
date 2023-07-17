package com.example;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApplicationTests {
	MockMvc mockMvc = initMockMvc();

	MockMvc initMockMvc() {
		StaticWebApplicationContext applicationContext = new StaticWebApplicationContext();
		applicationContext.registerBean(DispatcherServlet.HANDLER_MAPPING_BEAN_NAME,
				HandlerMapping.class, () -> {
					RouterFunctionMapping mapping = new RouterFunctionMapping(new Routing().routes());
					mapping.setMessageConverters(List.of(
							new StringHttpMessageConverter(),
							new ByteArrayHttpMessageConverter(),
							new AllEncompassingFormHttpMessageConverter(),
							new MappingJackson2HttpMessageConverter()
					));
					return mapping;
				});
		applicationContext.setServletContext(new MockServletContext());
		applicationContext.refresh();
		// https://github.com/spring-projects/spring-framework/issues/30477
		return MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}

	@Test
	void hello() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello World!"));
	}

}