package com.saber.camel.routes;

import com.saber.camel.entity.Person;
import io.quarkus.arc.Unremovable;
import org.apache.camel.Exchange;
import org.apache.camel.component.jpa.JpaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Unremovable
public class FindPersonByNationalCodeRoute extends AbstractRestRouteBuilder {

	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.get("/findByNationalCode/{nationalCode}")
				.id(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_ID)
				.responseMessage().code(200).responseModel(Person.class).endResponseMessage()
				.produces(MediaType.APPLICATION_JSON)
				.enableCORS(true)
				.param().name("nationalCode").type(RestParamType.path).dataType("string").required(true).example("0079028748").endParam()
				.bindingMode(RestBindingMode.json)
				.route()
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.log("Request for find person by nationalCode  route  with nationalCode : ${in.header.nationalCode}")
				.to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
				.routeId(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
				.routeGroup(Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
				.process(exchange -> {
					String nationalCode=  exchange.getIn().getHeader("nationalCode",String.class);
					Map<String,Object> parameters = new HashMap<>();
					parameters.put("nationalCode",nationalCode);
					exchange.getIn().setHeader(JpaConstants.JPA_PARAMETERS_HEADER,parameters);
				})
				.to("jpa:" + Person.class.getName()+"?namedQuery=findPersonByNationalCode")
				.log("Response for find person by nationalCode  from table ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
		
	}
}