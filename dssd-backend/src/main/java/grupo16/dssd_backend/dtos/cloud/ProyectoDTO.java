package grupo16.dssd_backend.dtos.cloud;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProyectoDTO {

//    private List<PedidoColaboracionDTO> pedidosColaboracion;
    private CloudUserDTO cargadoPor;

}
