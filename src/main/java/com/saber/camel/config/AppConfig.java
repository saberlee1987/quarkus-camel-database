package com.saber.camel.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.smallrye.openapi.api.models.ComponentsImpl;
import io.smallrye.openapi.api.models.OpenAPIImpl;
import io.smallrye.openapi.api.models.info.InfoImpl;
import io.smallrye.openapi.api.models.security.OAuthFlowImpl;
import io.smallrye.openapi.api.models.security.OAuthFlowsImpl;
import io.smallrye.openapi.api.models.security.SecurityRequirementImpl;
import io.smallrye.openapi.api.models.security.SecuritySchemeImpl;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.security.SecurityScheme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Singleton;

@ApplicationScoped
public class AppConfig {
	
	@ConfigProperty(name = "quarkus.swagger-ui.authorizationUrl")
	String authorizationUrl;
	
	@ConfigProperty(name = "quarkus.swagger-ui.tokenUrl")
	String tokenUrl;
	
	@Singleton
	@Named(value = "mapper")
	public ObjectMapper mapper() {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);
		
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		return mapper;
	}
	
	@ApplicationScoped
	@Named(value = "openapi")
	public OpenAPI configOpenApi() {
		String bearerSecuritySchema = "keycloak-authorize";
		return new OpenAPIImpl()
				.addSecurityRequirement(new SecurityRequirementImpl().addScheme(bearerSecuritySchema))
				.components(new ComponentsImpl()
						.addSecurityScheme(bearerSecuritySchema, new SecuritySchemeImpl()
								.name(bearerSecuritySchema)
								.type(SecurityScheme.Type.OAUTH2)
								.flows(new OAuthFlowsImpl().authorizationCode(
										new OAuthFlowImpl().authorizationUrl(authorizationUrl).tokenUrl(tokenUrl)
												.addScope("openid", "openid")
								)))
				)
				.info(new InfoImpl()
						.title("camel quarkus database")
						.version("version1.0.1-1400/11/11")
						.description("camel quarkus database")
				)
				
				;
	}
}
