package grupo16.dssd_backend.services.cloud;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.models.Proyecto;

import java.util.List;

public interface I_CloudService {

    void authenticate();

    ProyectoDTO getProyectoDetails(Proyecto proyecto);

    // Request a api cloud -> /api/v1/proyectos
    List<ProyectoCloudDTO> getAllProyectos();
}
