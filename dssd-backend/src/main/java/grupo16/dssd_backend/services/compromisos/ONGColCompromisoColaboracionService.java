package grupo16.dssd_backend.services.compromisos;

import com.fasterxml.jackson.core.JsonProcessingException;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ONGColCompromisoColaboracionService extends AbstractCompromisoColaboracionService{

    @Override
    public ColaboracionDTO createColaboracion(ColaboracionDTO colaboracionDTO, Long idProyecto, Long idPedido) throws RoleException {
        Proyecto proyecto = this.proyectoRepository.findById(idProyecto)
                .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"));

        ProyectoDTO proyectoDTO = ProyectoDTO.fromEntity(proyecto);

        String colaboracionJson = null;
        try {
            colaboracionJson = mapper.writeValueAsString(colaboracionDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        bonitaService.setVariablesCase(proyecto.getCaseId().toString(),Map.of(
                "idProyectoCloud", proyecto.getExternalId(),
                "idPedidoCloud", idPedido,
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
