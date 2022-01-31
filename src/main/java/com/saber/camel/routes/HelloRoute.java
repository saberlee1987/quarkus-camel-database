package com.saber.camel.routes;

import com.saber.camel.dto.HelloDto;
import io.quarkus.arc.Unremovable;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import javax.inject.Singleton;

@Singleton
@Unremovable
public class HelloRoute extends AbstractRestRouteBuilder {
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/hello")
				.get("/sayHello")
				.id(Routes.SAY_HELLO_ROUTE_ID)
				.description("say hello")
				.responseMessage().code(200).message("Success").responseModel(HelloDto.class).endResponseMessage()
				.enableCORS(true)
				.bindingMode(RestBindingMode.json)
				.param().name("firstName").type(RestParamType.query).dataType("string").example("saber").required(true).endParam()
				.param().name("lastName").type(RestParamType.query).dataType("string").example("Azizi").required(true).endParam()
				.route()
				.routeId(Routes.SAY_HELLO_ROUTE)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.to(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.SAY_HELLO_ROUTE_GATEWAY))
				.routeId(Routes.SAY_HELLO_ROUTE_GATEWAY)
				.routeGroup(Routes.SAY_HELLO_ROUTE_GROUP)
				.log("Request for say Hello with firstName : ${in.header.firstName} and lastName : ${in.header.lastName}")
				.process(exchange -> {
					String firstName = exchange.getIn().getHeader("firstName", String.class);
					String lastName = exchange.getIn().getHeader("lastName", String.class);
					
					HelloDto dto = new HelloDto();
					dto.setMessage(String.format("Hello %s %s", firstName, lastName));
					exchange.getIn().setBody(dto);
				})
				.log("Response for say Hello ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
	}
}
