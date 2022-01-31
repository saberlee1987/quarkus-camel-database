package com.saber.camel.routes;

import com.saber.camel.entity.Person;
import io.quarkus.arc.Unremovable;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
@Unremovable
public class FindAllPersonRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findAll")
				.id(Routes.FIND_ALL_PERSON_ROUTE_ID)
				.responseMessage().code(200).responseModel(Person.class).endResponseMessage()
				.produces(MediaType.APPLICATION_JSON)
				.enableCORS(true)
				.bindingMode(RestBindingMode.json)
				.route()
				.routeId(Routes.FIND_ALL_PERSON_ROUTE)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.log("Request for find all person route .....................")
				.to(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
				.to("jpa:" + Person.class.getName()+"?query={{service.database.query.findall}}")
				.log("Response for find all persons from table ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
		
	}
}
