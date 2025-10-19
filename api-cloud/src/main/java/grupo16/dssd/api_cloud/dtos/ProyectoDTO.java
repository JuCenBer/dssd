package grupo16.dssd.api_cloud.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class ProyectoDTO {

    private Long id;

    private String name;

    private Long caseId;

    private String description;

    private String ubicacion;

    private List<PedidoColaboracionDTO> pedidosColaboracion;

    private UserDTO cargadoPor;
}
