package grupo16.dssd.api_cloud.dtos.bonita;

import java.util.List;

public record CreacionProyectoDTO(
        Long id,
        String nombre,
        Long caseId,
        String descripcion,
        String ubicacion,
        List<CreacionActividadDTO> actividades
) {
}
