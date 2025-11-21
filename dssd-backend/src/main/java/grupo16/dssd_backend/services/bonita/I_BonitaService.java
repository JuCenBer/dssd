package grupo16.dssd_backend.services.bonita;

import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.models.Proyecto;

import java.util.List;
import java.util.Map;

public interface I_BonitaService {

    BonitaSession loginAndReturnCookies(String username, String password);

    void logout(BonitaSession session);

    Long instanciarProcesoCreacionProyecto(Proyecto proyecto);

    void ejecutarSiguienteTareaReady(Long caseId);

    BonitaSession getUserRole();

    List<Long> getUserProcessesCaseIds(String processName);

    void setVariablesCase(String caseId, Map<String, Object> variables);
}
