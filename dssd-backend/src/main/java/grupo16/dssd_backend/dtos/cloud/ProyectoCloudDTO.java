package grupo16.dssd_backend.dtos.cloud;

import grupo16.dssd_backend.models.EstadoProyecto;

public record ProyectoCloudDTO(

    Long id,
    String nombre,
    Long caseId,
    String descripcion,
    String ubicacion,
    CloudUserDTO cargadoPor,
    EstadoProyecto estado
) {
}
