package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.models.Actividad;
import grupo16.dssd_backend.models.EstadoProyecto;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ONGColProyectoService extends AbstractProyectoService {

    @Override
    protected List<ProyectoCloudDTO> applyRoleFilter(List<ProyectoCloudDTO> proyectos) {
        return proyectos;
    }

    @Override
    public Role roleForService() {
        return Role.ONG_COL;
    }

}
