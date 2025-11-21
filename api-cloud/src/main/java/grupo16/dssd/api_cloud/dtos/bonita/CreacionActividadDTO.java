package grupo16.dssd.api_cloud.dtos.bonita;

import grupo16.dssd.api_cloud.models.Recurso;

import java.time.LocalDate;

public record CreacionActividadDTO(
        Long id,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Recurso recurso,
        boolean requiereColaboracion
) {
}
