package grupo16.dssd_backend.services.cloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd_backend.config.CloudProperties;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.models.Proyecto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class CloudService implements I_CloudService {

    private CloudProperties properties;
    private final RestClient client;
    private volatile String jwtToken;

    @Autowired
    private ObjectMapper objectMapper;

    public CloudService(CloudProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder()
                .messageConverters(converters -> {
                    converters.add(new MappingJackson2HttpMessageConverter());
                })
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public void authenticate() {
        // Body de login (depende de la API real)
        Map<String, String> body = Map.of(
                "username", properties.getUsername(),
                "apiKey", properties.getApiKey()
        );

        var response = client.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        if (response == null || response.get("token") == null) {
            throw new IllegalStateException("No se recibió token JWT de autenticación.");
        }

        this.jwtToken = response.get("token").asText();
    }

    @Override
    // Request a api cloud -> /api/v1/proyectos/1?completo=true
    public ProyectoDTO getProyectoDetails(Proyecto proyecto) {
        if(this.jwtToken == null){
            this.authenticate();
        }

        var response = client.get()
                .uri("/proyectos/"+proyecto.getExternalId()+"?completo=true")
                .retrieve()
                .body(JsonNode.class);

        if (response == null || response.get("proyecto") == null) {
            throw new IllegalStateException("No se recibió informacion del proyecto.");
        }

        ProyectoDTO proyectoDTO = null;
        try {
            proyectoDTO = objectMapper.treeToValue(
                    response.get("proyecto"),
                    ProyectoDTO.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return proyectoDTO;
    }
}
