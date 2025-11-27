package grupo16.dssd.api_cloud.services.proyecto;

import grupo16.dssd.api_cloud.dtos.ObservacionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoCompletoDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.dtos.bonita.CreacionProyectoDTO;
import grupo16.dssd.api_cloud.models.EstadoProyecto;
import grupo16.dssd.api_cloud.models.Observacion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;

import java.util.List;
import java.util.Optional;

public interface I_ProyectoService {

    Optional<Proyecto> findById(Long id);

    ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO, User cargadoPor);

    ProyectoDTO crearProyecto(CreacionProyectoDTO proyectoDTO, User cargadoPor);

    List<ProyectoDTO> findAll();

    Optional<Proyecto> findByCaseId(Long id);

    Proyecto updateEstado(Proyecto proyecto, EstadoProyecto estado);

    ObservacionDTO agregarObservacion(Proyecto proyecto, ObservacionDTO observacion, User hechoPor);

    ProyectoCompletoDTO resolverObservacion(Proyecto proyecto, Long idObservacion);
}
