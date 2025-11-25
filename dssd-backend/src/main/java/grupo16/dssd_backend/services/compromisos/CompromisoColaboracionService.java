package grupo16.dssd_backend.services.compromisos;

import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.services.bonita.I_BonitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompromisoColaboracionService {

    @Autowired
    protected I_BonitaService bonitaService;

    @Transactional
    public ColaboracionDTO createColaboracion(ColaboracionDTO colaboracionDTO) throws RoleException{


        return colaboracionDTO;
    }
}
