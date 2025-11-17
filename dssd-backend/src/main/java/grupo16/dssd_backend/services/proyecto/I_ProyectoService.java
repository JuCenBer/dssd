package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Role;

import java.util.List;

public interface I_ProyectoService {

    void createProject(ProyectoDTO proyectoDTO) throws RoleException, ValidationException;

    List<Integer> getProyectos();

    Role roleForService();
}
