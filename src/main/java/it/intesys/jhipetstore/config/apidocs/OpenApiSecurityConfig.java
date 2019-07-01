package it.intesys.jhipetstore.config.apidocs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import it.intesys.jhipetstore.service.OpenApiService;

@Configuration
@Order(1)
public class OpenApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OpenApiService openApiService;

    public OpenApiSecurityConfig(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(openApiService.getSpecLocation())
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .csrf().disable();
    }
}
