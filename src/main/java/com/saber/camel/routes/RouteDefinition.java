package com.saber.camel.routes;

import io.quarkus.arc.Unremovable;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.inject.Singleton;

@Singleton
@Unremovable
public class RouteDefinition extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		
		restConfiguration()
				.contextPath("{{service.api.base-path}}")
				.apiContextPath("{{service.api.swagger-path}}")
				.apiProperty("api.title","{{service.api.swagger-title}}")
				.apiProperty("api.title","{{service.api.swagger-version}}")
				.apiProperty("cors","true")
				.enableCORS(true)
				.bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint","true");
				
	}
}
