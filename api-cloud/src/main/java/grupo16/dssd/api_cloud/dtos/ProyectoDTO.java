package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.EstadoProyecto;
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

//    private List<PedidoColaboracionDTO> pedidosColaboracion;

    private UserDTO cargadoPor;

    private EstadoProyecto estado;

    public static ProyectoDTO fromEntity(Proyecto proyecto) {
        return ProyectoDTO.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .caseId(proyecto.getCaseId())
                .descripcion(proyecto.getDescripcion())
                .ubicacion(proyecto.getUbicacion())
//                .pedidosColaboracion()
                .cargadoPor(UserDTO.fromEntity(proyecto.getCargadoPor()))
                .estado(proyecto.getEstado())
                .build();
    }

    public static List<ProyectoDTO> fromEntity(List<Proyecto> proyectos) {
        return proyectos.stream()
                .map(ProyectoDTO::fromEntity)
                .toList();
    }
}
