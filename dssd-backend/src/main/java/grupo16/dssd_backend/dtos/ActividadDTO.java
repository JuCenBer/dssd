package grupo16.dssd_backend.dtos;

import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.models.Actividad;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Builder
public record ActividadDTO(
        Long id,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String recurso,
        Boolean requiereColaboracion,
        ColaboracionDTO colaboracion

) {

    static ActividadDTO fromEntity(Actividad actividad) {
        return ActividadDTO.builder()
                .id(actividad.getId())
                .nombre(actividad.getNombre())
                .fechaInicio(actividad.getFechaInicio())
                .fechaFin(actividad.getFechaFin())
                .recurso(actividad.getRecurso().name())
                .requiereColaboracion(actividad.getRequiereColaboracion())
                .colaboracion(null)
                .build();
    }

    static List<ActividadDTO> fromEntity(Collection<Actividad> actividades) {
        return actividades.stream()
                .map(ActividadDTO::fromEntity)
                .toList();
    }
}
