package grupo16.dssd_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloud")
@Getter @Setter
public class CloudProperties {

    private String username;
    private String apiKey;
    private String baseUrl;

}