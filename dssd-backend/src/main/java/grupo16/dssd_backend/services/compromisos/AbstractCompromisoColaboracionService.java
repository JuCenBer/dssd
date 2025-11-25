package grupo16.dssd_backend.services.compromisos;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.models.Role;
import grupo16.dssd_backend.repositories.ProyectoRepository;
import grupo16.dssd_backend.services.bonita.I_BonitaService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractCompromisoColaboracionService implements I_CompromisoColaboracionService{

    @Autowired
    protected I_BonitaService bonitaService;

    @Autowired
    protected ProyectoRepository proyectoRepository;

    @Autowired
    protected ObjectMapper mapper;

    @Override
    public ColaboracionDTO createColaboracion(ColaboracionDTO colaboracionDTO, Long idProyecto, Long idPedido) throws RoleException {
        return null;
    }

    @Override
    public Role roleForService() {
        return null;
    }


}
