package grupo16.dssd_backend.services.bonita;

import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.helpers.NombresProcesos;
import grupo16.dssd_backend.models.Proyecto;

import java.util.List;

public interface I_BonitaService {

    BonitaSession loginAndReturnCookies(String username, String password);

    void logout(BonitaSession session);

    Long iniciarProcesoCreacionProyecto(Proyecto proyecto);

    BonitaSession getUserRole();

    List<Long> getUserProcessesCaseIds(String processName);

}
