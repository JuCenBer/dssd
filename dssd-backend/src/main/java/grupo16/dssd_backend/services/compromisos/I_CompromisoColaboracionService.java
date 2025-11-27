package grupo16.dssd_backend.services.compromisos;

import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.models.Role;

public interface I_CompromisoColaboracionService {

     ColaboracionDTO createColaboracion(ColaboracionDTO colaboracionDTO, Long proyectoExternalId, Long idPedido) throws RoleException;

     Role roleForService();
}
