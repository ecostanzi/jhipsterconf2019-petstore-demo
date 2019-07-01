package it.intesys.jhipetstore.web;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import it.intesys.jhipetstore.service.OpenApiService;

/**
 * Controller needed to serve the yml file stored into /resources/swagger/api.yml in a 'almost-static' way.
 * This allows to have the exposed yml file exactly equals as the one in the yml file and avoiding springfox to generate the documentation.
 */
@Controller
@RequestMapping("${openapi.jhipetstore.base-path:/api-first}")
@Profile(JHipsterConstants.SPRING_PROFILE_SWAGGER)
public class OpenApiController {

    private final OpenApiService openApiService;

    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @GetMapping(value = "/api.yml", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<byte[]> getApis(HttpServletRequest httpServletRequest) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/application+json");
        String apiEndpoint = httpServletRequest.getRequestURL().toString()
            .replace("/api.yml", "");
        return new ResponseEntity<>(openApiService.getContent(apiEndpoint), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getApiVersion() throws IOException {
        return ResponseEntity.ok(Collections.singletonMap("version", openApiService.getApiVersion()));
    }

}
