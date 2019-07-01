package it.intesys.jhipetstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class OpenApiService {

    public static final String OPENAPI_SPEC_FILE = "api.yml";

    private final ObjectMapper ymlObjectMapper = new ObjectMapper(new YAMLFactory());

    private final JsonNode ymlJsonNode;
    private final String basePath;

    public OpenApiService(@Value("${openapi.jhipetstore.base-path:/api-first}") String basePath) {
        this.basePath = basePath;
        try {
            ClassPathResource classPathResource = new ClassPathResource("swagger/" + OPENAPI_SPEC_FILE);
            byte[] ymlContent = IOUtils.toByteArray(classPathResource.getInputStream());
            ymlJsonNode = ymlObjectMapper.readTree(ymlContent);
        } catch (Exception e){
            throw new IllegalStateException("Failed to load api yml file", e);
        }
    }

    /**
     * Returns the yml file content. Replaces servers list to add the endpoint passed as parameter
     * @param apiEndpoint the api endpoint to be inserted into the yml file
     * @return the content of the yml, with the dynamically replaced server list
     * @throws IOException
     */
    public byte[] getContent(String apiEndpoint) throws IOException {
        ArrayNode servers = (ArrayNode) ymlJsonNode.get("servers");
        if(servers != null){
            //remove servers (if any)
            servers.removeAll();
        } else {
            servers = ymlObjectMapper.createArrayNode();
        }

        ObjectNode objectNode = ymlObjectMapper.createObjectNode();
        objectNode.put("url", apiEndpoint);
        servers.add(objectNode);

        return ymlObjectMapper.writeValueAsBytes(ymlJsonNode);
    }


    /**
     * Returns the api version written into the api.yml file
     * @return the api version
     * @throws IOException
     */
    public String getApiVersion() {
        return ymlJsonNode.get("info").get("version").textValue();
    }

    /**
     * Openapi version (swagger 2.0, openapi 3.0)
     * @return the spec version (swagger/openapi)
     * @throws IOException
     */
    public String getSpecVersion() {
        if(ymlJsonNode.has("openapi")) {
            return ymlJsonNode.get("openapi").textValue();
        } else if(ymlJsonNode.has("swagger")){
            return ymlJsonNode.get("swagger").textValue();
        } else {
            return null;
        }
    }

    public String getSpecLocation(){
        return basePath + "/" + OPENAPI_SPEC_FILE;
    }

}
