package grupo16.dssd_backend.services.compromisos;

import com.fasterxml.jackson.core.JsonProcessingException;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ONGColCompromisoColaboracionService extends AbstractCompromisoColaboracionService{

    @Override
    public ColaboracionDTO createColaboracion(ColaboracionDTO colaboracionDTO, Long proyectoExternalId, Long idPedido) throws RoleException {
        Proyecto proyecto = this.proyectoRepository.findByExternalId(proyectoExternalId)
                .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"));

        String colaboracionJson = null;
        try {
            colaboracionJson = mapper.writeValueAsString(colaboracionDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Obtener tareas del caso
        List<Map<String, Object>> tareas =
                bonitaService.buscarTareasReadyPorCaso(proyecto.getCaseId().toString());

        bonitaService.setVariablesCase(proyecto.getCaseId().toString(),Map.of(
                "idProyectoCloud", proyecto.getExternalId().toString(),
                "idPedidoCloud", idPedido.toString(),
                "colaboracionJson", colaboracionJson
        ));

        bonitaService.ejecutarSiguienteTareaReady(proyecto.getCaseId());

        return colaboracionDTO;
    }

    @Override
    public Role roleForService() {
        return Role.ONG_COL;
    }
}
