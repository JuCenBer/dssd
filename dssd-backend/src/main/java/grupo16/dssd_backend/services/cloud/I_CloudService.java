package grupo16.dssd_backend.services.cloud;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.models.Proyecto;

public interface I_CloudService {

    void authenticate();

    ProyectoDTO getProyectoDetails(Proyecto proyecto);
}
