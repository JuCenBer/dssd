package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ObservacionCloudDTO;
import grupo16.dssd_backend.dtos.cloud.ProyectoCloudDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.EstadoProyecto;
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

    @Override
    public void agregarObservacion(Long externalId, ObservacionCloudDTO observacion) throws RoleException, ValidationException {
        if (observacion.comentario().isBlank()) {
            throw new ValidationException("No puede agregar una observación en blanco.");
        }

        Proyecto proyecto = this.proyectoRepository.findByExternalId(externalId)
                .orElseThrow();

        ProyectoCloudDTO proyectoCloud = this.cloudService.getProyectoDetails(proyecto);

        if (!proyectoCloud.estado().equals(EstadoProyecto.EN_EJECUCION)) {
            throw new ValidationException("Sólo se puede agregar una observación a un proyecto en ejecución.");
        }

        // Bonita
        Long caseId = this.bonitaService.instanciarProcesoControlProyecto(proyecto, observacion);

        this.bonitaService.ejecutarSiguienteTareaReady(caseId);

    }
}
