package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ObservacionCloudDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Role;

import java.util.List;

public interface I_ProyectoService {

    ProyectoDTO createProject(ProyectoDTO proyectoDTO) throws RoleException, ValidationException;

    List<ProyectoCloudDTO> getProyectos();

    Role roleForService();

    ProyectoDTO getProyecto(Long proyectoId) throws ValidationException;

    ProyectoDTO getProyectoByExternalId(Long externalId) throws ValidationException;

    void finalizarProyecto(Long externalId) throws ValidationException, RoleException;

    void agregarObservacion(Long externalId, ObservacionCloudDTO observacion) throws RoleException, ValidationException;
}
