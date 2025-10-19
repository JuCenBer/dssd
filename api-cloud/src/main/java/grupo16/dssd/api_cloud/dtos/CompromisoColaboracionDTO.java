package grupo16.dssd.api_cloud.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class CompromisoColaboracionDTO {

    private Long id;
    private UserDTO userCompromiso;
    private String descripcion;
    private PedidoColaboracionDTO pedidoColaboracion;
    private Boolean cumplido = false;
}
