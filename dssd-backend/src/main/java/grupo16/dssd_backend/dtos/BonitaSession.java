package grupo16.dssd_backend.dtos;

import grupo16.dssd_backend.models.Role;

public record BonitaSession(
        String username,
        String jsessionId,
        String xBonitaToken,
        long createdAtEpochMs,
        Role role,
        Integer userId
) {}
