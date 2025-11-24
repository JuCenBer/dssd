package grupo16.dssd_backend.dtos.cloud;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CloudUserDTO {

    private String username;
    private String nombreOng;

}
