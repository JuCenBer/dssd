package grupo16.dssd.api_cloud.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import grupo16.dssd.api_cloud.models.EstadoProyecto;
import grupo16.dssd.api_cloud.models.Proyecto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProyectoCompletoDTO {

    private Long id;

    private String nombre;

    private Long caseId;

    private String descripcion;

    private String ubicacion;

    private List<PedidoColaboracionCompletoDTO> pedidosColaboracion;

    private EstadoProyecto estado;

    private UserDTO cargadoPor;

    public static ProyectoCompletoDTO fromEntity(Proyecto proyecto) {
        return ProyectoCompletoDTO.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .caseId(proyecto.getCaseId())
                .descripcion(proyecto.getDescripcion())
                .ubicacion(proyecto.getUbicacion())
                .pedidosColaboracion(PedidoColaboracionCompletoDTO.fromEntity(proyecto.getPedidosColaboracion()))
                .cargadoPor(UserDTO.fromEntity(proyecto.getCargadoPor()))
                .estado(proyecto.getEstado())
                .build();
    }

    public static List<ProyectoCompletoDTO> fromEntity(List<Proyecto> proyectos) {
        return proyectos.stream()
                .map(ProyectoCompletoDTO::fromEntity)
                .toList();
    }
}
