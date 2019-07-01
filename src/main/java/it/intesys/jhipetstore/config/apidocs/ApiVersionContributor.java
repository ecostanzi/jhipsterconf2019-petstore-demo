package it.intesys.jhipetstore.config.apidocs;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import it.intesys.jhipetstore.service.OpenApiService;

import java.util.HashMap;
import java.util.Map;

/**
 * This Contributor will be picked up by {@link org.springframework.boot.actuate.info.InfoEndpoint}
 */
@Component
@Profile(JHipsterConstants.SPRING_PROFILE_SWAGGER)
public class ApiVersionContributor implements InfoContributor {

    private final OpenApiService openApiService;

    public ApiVersionContributor(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> openapiInfo = new HashMap<>();
        openapiInfo.put("version", openApiService.getApiVersion());
        openapiInfo.put("specVersion", openApiService.getSpecVersion());
        openapiInfo.put("location", openApiService.getSpecLocation());
        builder.withDetail("openapi", openapiInfo);
    }
}
