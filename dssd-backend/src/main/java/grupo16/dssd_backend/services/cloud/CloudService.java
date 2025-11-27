package grupo16.dssd_backend.services.cloud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import grupo16.dssd_backend.config.CloudProperties;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.models.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CloudService implements I_CloudService {

    private CloudProperties properties;
    private final RestTemplate restTemplate;
    private volatile String jwtToken;

    @Autowired
    private ObjectMapper objectMapper;

    public CloudService(CloudProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();

    }

    public void authenticate() {
        try {
            // Construimos el JSON manualmente
            ObjectNode loginRequest = objectMapper.createObjectNode();
            loginRequest.put("username", properties.getUsername());
            loginRequest.put("apiKey", properties.getApiKey());

            // Encabezados
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Request completo
            HttpEntity<String> request =
                    new HttpEntity<>(loginRequest.toString(), headers);

            // Hacemos la llamada POST
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    properties.getBaseUrl() + "/api/v1/auth/login",
                    HttpMethod.POST,
                    request,
                    JsonNode.class
            );

            // Validación
            if (response.getBody() == null ||
                    response.getBody().get("token") == null) {
                throw new IllegalStateException("No se recibió token JWT de autenticación.");
            }

            // Guardamos token
            this.jwtToken = response.getBody().get("token").asText();

        } catch (Exception ex) {
            throw new RuntimeException("Error autenticando con la API", ex);
        }

    }


    @Override
    // Request a api cloud -> /api/v1/proyectos/1?completo=true
    public ProyectoCloudDTO getProyectoDetails(Proyecto proyecto) {

        if (this.jwtToken == null) {
            this.authenticate();
        }

        try {
            // Headers con el token
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(this.jwtToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            String url = properties.getBaseUrl() +
                    "/api/v1/proyectos/" + proyecto.getExternalId() +
                    "?completo=true";

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    JsonNode.class
            );

            JsonNode body = response.getBody();

            if (body == null || body.isEmpty()) {
                throw new IllegalStateException("No se recibió información del proyecto.");
            }

            return objectMapper.treeToValue(body,ProyectoCloudDTO.class);

        } catch (HttpClientErrorException.Unauthorized e) {
            this.authenticate();
            return this.getProyectoDetails(proyecto);
        }catch (Exception e) {
            throw new RuntimeException("Error obteniendo el proyecto", e);
        }

    }

    @Override
    public List<ProyectoCloudDTO> getAllProyectos() {

        if (this.jwtToken == null) {
            this.authenticate();
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(this.jwtToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            String url = properties.getBaseUrl() + "/api/v1/proyectos";

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    JsonNode.class
            );

            JsonNode body = response.getBody();

            if (body == null || body.isEmpty()) {
                return List.of();
            }

            return objectMapper.convertValue(
                    body,
                    new TypeReference<List<ProyectoCloudDTO>>() {}
            );

        } catch (HttpClientErrorException.Unauthorized e) {
            this.authenticate();
            return this.getAllProyectos();
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo los proyectos", e);
        }
    }



}
