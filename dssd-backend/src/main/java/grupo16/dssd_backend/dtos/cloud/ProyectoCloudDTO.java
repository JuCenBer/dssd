package grupo16.dssd_backend.dtos.cloud;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import grupo16.dssd_backend.dtos.ActividadDTO;
import grupo16.dssd_backend.models.EstadoProyecto;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ProyectoCloudDTO(

    Long id,
    String nombre,
    Long caseId,
    String descripcion,
    String ubicacion,
    CloudUserDTO cargadoPor,
    EstadoProyecto estado,
    @JsonAlias({"pedidosColaboracion","actividades"})
    List<ActividadDTO> actividades
) {
}
