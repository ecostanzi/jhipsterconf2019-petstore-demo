package it.intesys.jhipetstore.config.apidocs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import it.intesys.jhipetstore.service.OpenApiService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApiFirstConsulCustomizer {

    @Bean
    @Primary
    public ConsulRegistrationCustomizer apiFirstServiceCustomizer(OpenApiService openApiService,
                                                                  @Value("${spring.application.name}") String appName) {
        return registration -> {
            List<String> tags = registration.getService().getTags();
            if(tags == null){
                tags = new ArrayList<>();
            }
            tags.add("api=" + openApiService.getApiVersion());

            //fabio upstream rules
            String appNamePath = "/" + appName;
            tags.add(String.format("urlprefix-%s strip=%s", appNamePath, appNamePath));

            String apiFirstPath = "/" + appName + "/api/v" + openApiService.getApiVersion().split("\\.")[0];
            tags.add(String.format("urlprefix-%s strip=%s", apiFirstPath, apiFirstPath));

        };
    }
}
