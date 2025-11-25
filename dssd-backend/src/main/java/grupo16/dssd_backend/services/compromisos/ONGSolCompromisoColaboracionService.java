package grupo16.dssd_backend.services.compromisos;

import grupo16.dssd_backend.models.Role;
import org.springframework.stereotype.Service;


@Service
public class ONGSolCompromisoColaboracionService extends AbstractCompromisoColaboracionService{

    @Override
    public Role roleForService() {
        return Role.ONG_SOL;
    }
}
