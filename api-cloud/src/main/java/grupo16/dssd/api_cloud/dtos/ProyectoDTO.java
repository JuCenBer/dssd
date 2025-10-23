package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.Proyecto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class ProyectoDTO {

    private Long id;

    private String nombre;

    private Long caseId;

    private String descripcion;

    private String ubicacion;

    private List<PedidoColaboracionDTO> pedidosColaboracion;

//    private UserDTO cargadoPor;

    public static ProyectoDTO fromEntity(Proyecto proyecto) {
        return ProyectoDTO.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .caseId(proyecto.getCaseId())
                .descripcion(proyecto.getDescripcion())
                .ubicacion(proyecto.getUbicacion())
//                .pedidosColaboracion()
//                .cargadoPor(proyecto.getCargadoPor())
                .build();
    }
}
