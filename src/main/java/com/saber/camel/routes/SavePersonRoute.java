package com.saber.camel.routes;

import com.saber.camel.dto.PersonDto;
import com.saber.camel.entity.Person;
import io.quarkus.arc.Unremovable;
import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
@Unremovable
public class SavePersonRoute extends AbstractRestRouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		rest("/persons")
				.post("/save")
				.id(Routes.SAVE_PERSON_ROUTE_ID)
				.responseMessage().code(200).responseModel(Person.class).example("example1","{\"firstName\": \"saber\",\"lastName\": \"azizi\",\"nationalCode\": \"0079028748\",\"age\": 35}").endResponseMessage()
				.consumes(MediaType.APPLICATION_JSON)
				.produces(MediaType.APPLICATION_JSON)
				.enableCORS(true)
				.type(PersonDto.class)
				.bindingMode(RestBindingMode.json)
				.route()
				.routeId(Routes.SAVE_PERSON_ROUTE)
				.routeGroup(Routes.SAVE_PERSON_ROUTE_GROUP)
				.log("Request for add person with body ${in.body}")
				.to(String.format("direct:%s", Routes.SAVE_PERSON_ROUTE_GATEWAY));
		
		from(String.format("direct:%s", Routes.SAVE_PERSON_ROUTE_GATEWAY))
				.routeId(Routes.SAVE_PERSON_ROUTE_GATEWAY)
				.routeGroup(Routes.SAVE_PERSON_ROUTE_GROUP)
				.process(exchange -> {
					PersonDto personDto = exchange.getIn().getBody(PersonDto.class);
					
					Person person = new Person();
					person.setFirstName(personDto.getFirstName());
					person.setLastName(personDto.getLastName());
					person.setAge(personDto.getAge());
					person.setNationalCode(personDto.getNationalCode());
					
					exchange.getIn().setBody(person);
				})
				.transacted()
				.to("jpa:" + Person.class.getName())
				.log("Response for add person to table ===> ${in.body}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
		
	}
}
