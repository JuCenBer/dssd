package grupo16.dssd_backend.dtos.cloud;

public record ColaboracionDTO(
        Long id,
        CloudUserDTO userCompromiso,
        String descripcion
) {}
