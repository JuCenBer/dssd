package grupo16.dssd_backend.services.bonita;

import grupo16.dssd_backend.dtos.BonitaSession;

public interface I_BonitaService {

    BonitaSession loginAndReturnCookies(String username, String password);

    void logout(BonitaSession session);

    Long iniciarProcesoCreacionProyecto(String nombre);

    BonitaSession getUserRole();

//    Optional<String> getEnabledProcessIdByName(String processName);


//    Map<String, Object> instantiateProcess(String processId, Map<String, Object> contract);

//    List<Map<String, Object>> findReadyTasksByCase(String caseId);

//    void assignTask(String taskId, String userId);

//    void executeUserTask(String taskId, Map<String, Object> contract);
}
