package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectivoProyectoService extends AbstractProyectoService {

    @Override
    public Role roleForService() {
        return Role.DIRECTIVO;
    }

    @Override
    protected List<ProyectoCloudDTO> applyRoleFilter(List<ProyectoCloudDTO> proyectos) {
        return proyectos;
    }

}
