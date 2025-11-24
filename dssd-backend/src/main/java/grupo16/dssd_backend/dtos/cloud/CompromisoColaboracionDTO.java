package grupo16.dssd_backend.dtos.cloud;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CompromisoColaboracionDTO {

    private Long id;
    private CloudUserDTO userCompromiso;
    private String descripcion;
//    private Boolean cumplido;

}
