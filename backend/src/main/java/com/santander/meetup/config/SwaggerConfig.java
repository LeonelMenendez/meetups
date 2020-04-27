package com.santander.meetup.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.santander.meetup.endpoint.AuthEndpoint;
import com.santander.meetup.exceptions.ApiError;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.santander.meetup.security.JwtUtil.HEADER_AUTHORIZACION_KEY;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String[] SWAGGER_AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Autowired
    TypeResolver typeResolver;

    @Bean
    public Docket api() {
        List<ResponseMessage> responseMessages = getDefaultResponses();
        Set<String> DEFAULT_PRODUCES_AND_CONSUMES = ImmutableSet.of(MediaType.APPLICATION_JSON_VALUE);

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.santander.meetup.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .additionalModels(typeResolver.resolve(ApiError.class))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    /**
     * Defines information about the API.
     *
     * @return the information about the API.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Meetup REST API",
                "Santander Rio meetup challenge",
                "Version 1.0",
                "",
                new Contact("Leonel Menendez", "https://www.linkedin.com/in/leonel-menendez/", "leonelemenendez@gmail.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList()
        );
    }

    /**
     * Defines a new api key authorization added to header.
     *
     * @return a new api key authorization.
     */
    private ApiKey apiKey() {
        return new ApiKey(HEADER_AUTHORIZACION_KEY, HEADER_AUTHORIZACION_KEY, "header");
    }

    /**
     * Defines the security scope.
     *
     * @return the security scope.
     */
    private List<SecurityReference> jwtAuthReference() {
        return Arrays.asList(new SecurityReference(HEADER_AUTHORIZACION_KEY, new AuthorizationScope[0]));
    }

    /**
     * Defines the security context. It will apply to all endpoints that are not whitelisted.
     *
     * @return the security context.
     * @see #getEndPointsWhiteList()
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(jwtAuthReference()).forPaths(
                PathSelectors.regex("(?!" + getEndPointsWhiteList() + ").+")
        ).build();
    }

    /**
     * Returns a string made up of the endpoints excluded from authentication. Each of them separated by "|".
     *
     * @return the end points white list.
     */
    private String getEndPointsWhiteList() {
        StringBuilder whiteList = new StringBuilder();
        whiteList.append(AuthEndpoint.ROOT + AuthEndpoint.SIGN_UP + "|");
        whiteList.append(AuthEndpoint.ROOT + AuthEndpoint.SIGN_IN);
        return whiteList.toString();
    }

    /**
     * Returns a list with the default API responses.
     *
     * @return the default API responses.
     */
    private List<ResponseMessage> getDefaultResponses() {
        return Arrays.asList(
                createResponseMessage(HttpStatus.OK, "Successful operation"),
                createResponseMessage(HttpStatus.CREATED, "Resource created successfully"),
                createErrorResponseMessage(HttpStatus.BAD_REQUEST, "The server cannot or will not process the request due to an apparent client error"),
                createErrorResponseMessage(HttpStatus.UNAUTHORIZED, "You are not authorized to view the resource"),
                createErrorResponseMessage(HttpStatus.FORBIDDEN, "Accessing the resource you were trying to reach is forbidden"),
                createErrorResponseMessage(HttpStatus.NOT_FOUND, "The resource you were trying to reach is not found"),
                createErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Something has gone wrong on server")
        );
    }

    /**
     * Returns a new {@link ResponseMessage} with the given HTTP status and code.
     *
     * @param httpStatus the HTTP status.
     * @param message    the message.
     * @return a {@link ResponseMessage} instance with the given HTTP status and code.
     */
    private ResponseMessage createResponseMessage(HttpStatus httpStatus, String message) {
        return new ResponseMessageBuilder()
                .code(httpStatus.value())
                .message(message)
                .build();
    }

    /**
     * Returns a new {@link ResponseMessage} with an {@link ApiError} response model and the given HTTP
     * status and code.
     *
     * @param httpStatus the HTTP status.
     * @param message    the message.
     * @return a {@link ResponseMessage} instance with an {@link ApiError} and the given HTTP status and code.
     */
    private ResponseMessage createErrorResponseMessage(HttpStatus httpStatus, String message) {
        val httpStatus1 = httpStatus;
        return new ResponseMessageBuilder()
                .code(httpStatus.value())
                .message(message)
                .responseModel(new ModelRef("ApiError"))
                .build();
    }
}
