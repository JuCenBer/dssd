package grupo16.dssd_backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import grupo16.dssd_backend.models.Proyecto;

import java.util.Collection;
import java.util.List;

public record ProyectoDTO(
        Long id,
        String nombre,
        String descripcion,
        String ubicacion,
        Long caseId,
        List<ActividadDTO> actividades
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
        return new ProyectoDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getUbicacion(),
                proyecto.getCaseId(),
                ActividadDTO.fromEntity((proyecto.getActividades()))
        );
    }

    public static List<ProyectoDTO> fromEntity(Collection<Proyecto> proyectos) {
        return proyectos.stream()
                .map(ProyectoDTO::fromEntity)
                .toList();
    }
}