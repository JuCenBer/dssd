package grupo16.dssd_backend.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import grupo16.dssd_backend.dtos.cloud.ObservacionCloudDTO;
import grupo16.dssd_backend.models.EstadoProyecto;
import grupo16.dssd_backend.models.Proyecto;
import lombok.Builder;

import java.util.Collection;
import java.util.List;

@Builder
public record ProyectoDTO(
        Long id,
        String nombre,
        String descripcion,
        String ubicacion,
        Long caseId,

        @JsonAlias({"pedidosColaboracion","actividades"})
        List<ActividadDTO> actividades,

        List<ObservacionCloudDTO> observaciones,

        EstadoProyecto estado
) {

    @JsonIgnore
    public boolean isValid(){
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (descripcion == null || descripcion.isBlank()) {
            return false;
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            return false;
        }
        if (actividades == null || actividades.isEmpty()) {
            return false;
        }
        return true;
    }

    public static ProyectoDTO fromEntity(Proyecto proyecto) {
        return ProyectoDTO.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .descripcion(proyecto.getDescripcion())
                .ubicacion(proyecto.getUbicacion())
                .caseId(proyecto.getCaseId())
                .actividades(ActividadDTO.fromEntity((proyecto.getActividades())))
                .estado(proyecto.getEstado())
                .build();
    }

    public static List<ProyectoDTO> fromEntity(Collection<Proyecto> proyectos) {
        return proyectos.stream()
                .map(ProyectoDTO::fromEntity)
                .toList();
    }
}