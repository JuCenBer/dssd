package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Actividad;
import grupo16.dssd_backend.repositories.ActividadRepository;
import grupo16.dssd_backend.repositories.ProyectoRepository;
import grupo16.dssd_backend.services.bonita.I_BonitaService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractProyectoService implements I_ProyectoService {

    @Autowired
    protected ProyectoRepository proyectoRepository;

    @Autowired
    protected ActividadRepository actividadRepository;

    @Autowired
    protected I_BonitaService bonitaService;

    @Override
    public void createProject(ProyectoDTO proyectoDTO) throws RoleException, ValidationException {
        throw new RoleException("No tiene el rol necesario para realizar esta acción");
    }
}
