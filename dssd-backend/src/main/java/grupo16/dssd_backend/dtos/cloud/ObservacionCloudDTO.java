package grupo16.dssd_backend.dtos.cloud;

public record ObservacionCloudDTO(
        Long id,
        String comentario,
        Boolean resuelto,
        CloudUserDTO hechoPor,
        Long caseId
) {
}
