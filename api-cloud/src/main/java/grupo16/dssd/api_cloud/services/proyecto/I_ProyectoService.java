package grupo16.dssd.api_cloud.services.proyecto;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;

import java.util.Optional;

public interface I_ProyectoService {

    Optional<Proyecto> findById(Long id);

    ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO, User cargadoPor);
}
