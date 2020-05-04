package io.github.lzmz.meetups.config;

import io.github.lzmz.meetups.exceptions.ApiError;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
    };

    private static final String JSON_MEDIA_TYPE = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
    private static final String BASE_PACKAGE = "io.github.lzmz.meetups.controller";

    private static final String SECURITY_SCHEME = "bearer";
    private static final String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    private static final String API_ERROR_NAME = ApiError.class.getSimpleName();
    private static final Schema API_ERROR_SCHEMA = new Schema().$ref(API_ERROR_NAME);
    private static final MediaType MEDIA_TYPE = new MediaType().schema(API_ERROR_SCHEMA);
    private static final MediaType API_ERROR_MEDIA_TYPE = MEDIA_TYPE.schema(API_ERROR_SCHEMA);
    private static final Content API_ERROR_CONTENT = new Content().addMediaType(JSON_MEDIA_TYPE, API_ERROR_MEDIA_TYPE);

    /**
     * Defines information about the API.
     *
     * @return the information about the API.
     */
    private Info info() {
        return new Info()
                .title("Meetup REST API")
                .version("v1.0.0")
                .contact(contact())
                .license(license());
    }

    /**
     * Defines information about the author.
     *
     * @return the information about the author.
     */
    private Contact contact() {
        return new Contact().name("Leonel Menendez")
                .email("leonelemenendez@gmail.com")
                .url("https://www.linkedin.com/in/leonel-menendez/");
    }

    /**
     * Defines the license.
     *
     * @return the license.
     */
    private License license() {
        return new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html");
    }

    /**
     * Defines the security schema.
     *
     * @return the security schema.
     */
    public SecurityScheme security() {
        return new SecurityScheme()
                .scheme(SECURITY_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat(SECURITY_SCHEME_BEARER_FORMAT);
    }

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(SECURITY_SCHEME_NAME, security())
                                .addSchemas(API_ERROR_NAME, new Schema().$ref(API_ERROR_NAME))
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(info());

    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor(SpringDocConfigProperties springDocConfigProperties) {
        return beanFactory -> {
            springDocConfigProperties.setPackagesToScan(Arrays.asList(BASE_PACKAGE));
            springDocConfigProperties.setDefaultProducesMediaType(JSON_MEDIA_TYPE);
            springDocConfigProperties.setDefaultConsumesMediaType(JSON_MEDIA_TYPE);
        };
    }

    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            addApiErrorSchema(openApi);
            addDefaultGlobalResponses(openApi);
        };
    }

    /**
     * Adds the {@link ApiError} class to {@link OpenAPI} schemas.
     *
     * @param openApi the api to which will be added the error schema.
     */
    private void addApiErrorSchema(OpenAPI openApi) {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ApiError.class));
        openApi.schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
    }

    /**
     * Adds the default global responses of {@link OpenAPI}.
     *
     * @param openApi the api to which will be added the default global responses.
     */
    private void addDefaultGlobalResponses(OpenAPI openApi) {
        openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            addDefaultGlobalResponses(operation.getResponses());
        }));
    }

    /**
     * Adds the default global API responses.
     */
    private void addDefaultGlobalResponses(ApiResponses apiResponses) {
        apiResponses.replace(String.valueOf(HttpStatus.CREATED.value()), getDefaultGlobalResponse(HttpStatus.CREATED));
        apiResponses.replace(String.valueOf(HttpStatus.BAD_REQUEST.value()), getDefaultGlobalResponse(HttpStatus.BAD_REQUEST));
        apiResponses.replace(String.valueOf(HttpStatus.UNAUTHORIZED.value()), getDefaultGlobalResponse(HttpStatus.UNAUTHORIZED));
        apiResponses.replace(String.valueOf(HttpStatus.FORBIDDEN.value()), getDefaultGlobalResponse(HttpStatus.FORBIDDEN));
        apiResponses.replace(String.valueOf(HttpStatus.NOT_FOUND.value()), getDefaultGlobalResponse(HttpStatus.NOT_FOUND));
        apiResponses.replace(String.valueOf(HttpStatus.CONFLICT.value()), getDefaultGlobalResponse(HttpStatus.CONFLICT));
        apiResponses.replace(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), getDefaultGlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Returns a default global response for the given {@link HttpStatus}.
     * <p>If the given status is a 4xx client error or a 5xx server error, it will bind the {@link ApiError} to the schema of the response.</p>
     *
     * @param httpStatus the status of the global response.
     * @return the default global response.
     */
    private ApiResponse getDefaultGlobalResponse(HttpStatus httpStatus) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.description(httpStatus.getReasonPhrase());

        if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
            apiResponse.content(API_ERROR_CONTENT);
        }

        return apiResponse;
    }
}
