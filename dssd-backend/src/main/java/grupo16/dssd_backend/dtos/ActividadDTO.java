package grupo16.dssd_backend.dtos;

import grupo16.dssd_backend.models.Actividad;

import java.time.LocalDate;
import java.util.List;

public record ActividadDTO(
        Long id,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String recurso,
        boolean requiereColaboracion
) {

    static ActividadDTO fromEntity(Actividad actividad) {
        return new ActividadDTO(
                actividad.getId(),
                actividad.getNombre(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                actividad.getRecurso().name(),
                actividad.getRequiereColaboracion()
        );
    }

    static List<ActividadDTO> fromEntity(List<Actividad> actividades) {
        return actividades.stream()
                .map(ActividadDTO::fromEntity)
                .toList();
    }
}
