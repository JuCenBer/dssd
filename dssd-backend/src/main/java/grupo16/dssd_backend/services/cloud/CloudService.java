package grupo16.dssd_backend.services.cloud;

import com.fasterxml.jackson.databind.JsonNode;
import grupo16.dssd_backend.config.CloudProperties;
import grupo16.dssd_backend.models.Proyecto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class CloudService implements I_CloudService {

    private CloudProperties properties;
    private final RestClient client;
    private volatile String jwtToken;

    public CloudService(CloudProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder()
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
    public void getProyectoDetails(Proyecto proyecto) {

    }
}
