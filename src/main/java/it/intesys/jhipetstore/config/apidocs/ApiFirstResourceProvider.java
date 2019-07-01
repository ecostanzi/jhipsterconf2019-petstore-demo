package it.intesys.jhipetstore.config.apidocs;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import it.intesys.jhipetstore.service.OpenApiService;

import java.util.LinkedList;
import java.util.List;

@Component
@Primary
@Profile(JHipsterConstants.SPRING_PROFILE_SWAGGER)
public class ApiFirstResourceProvider extends InMemorySwaggerResourcesProvider implements SwaggerResourcesProvider {

    private final OpenApiService openApiService;

    public ApiFirstResourceProvider(Environment environment, DocumentationCache documentationCache, OpenApiService openApiService) {
        super(environment, documentationCache);
        this.openApiService = openApiService;
    }

    /**
     * Overrides springfox docket endpints list in order to add the endpoint for the static yml file to the /swagger-resources list.
     * @return the updated swagger endpoint list
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> swaggerResources = new LinkedList<>();

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName("openapi");
        swaggerResource.setUrl(openApiService.getSpecLocation());
        swaggerResource.setLocation(openApiService.getSpecLocation());
        swaggerResource.setSwaggerVersion(openApiService.getSpecVersion());
        swaggerResources.add(swaggerResource);

        swaggerResources.addAll(super.get());
        return swaggerResources;
    }
}
