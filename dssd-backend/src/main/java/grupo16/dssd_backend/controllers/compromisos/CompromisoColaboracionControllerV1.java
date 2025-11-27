package grupo16.dssd_backend.controllers.compromisos;

import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import grupo16.dssd_backend.models.Role;
import grupo16.dssd_backend.services.compromisos.I_CompromisoColaboracionService;
import grupo16.dssd_backend.services.proyecto.I_ProyectoService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/proyectos/{proyectoExternalId}/pedidos/{idPedido}/compromisos")
public class CompromisoColaboracionControllerV1 implements I_CompromisoColaboracionController {

    private final List<I_CompromisoColaboracionService> compromisoColaboracionServices;

    private Map<Role,I_CompromisoColaboracionService> compromisoColaboracionServiceMap;


    public CompromisoColaboracionControllerV1(List<I_CompromisoColaboracionService> compromisoColaboracionServices) {
        this.compromisoColaboracionServices = compromisoColaboracionServices;
    }

    @PostConstruct
    private void init() {
        this.compromisoColaboracionServiceMap = new HashMap<>();

        for (I_CompromisoColaboracionService service : this.compromisoColaboracionServices) {
            this.compromisoColaboracionServiceMap.put(service.roleForService(), service);
        }
    }

    private I_CompromisoColaboracionService getCorrectProyectoService() {
        BonitaSession session = BonitaSessionHolder.getBonitaSession();

        return this.compromisoColaboracionServiceMap.get(BonitaSessionHolder.getBonitaSession().role());
    }

    @PostMapping
    public ResponseEntity<?> crearCompromiso(@RequestBody ColaboracionDTO colaboracionDTO,
                                             @PathVariable("proyectoExternalId") Long proyectoExternalId,
                                             @PathVariable("idPedido") Long idPedido){

        try {
            ColaboracionDTO colaboracion = this.getCorrectProyectoService().createColaboracion(colaboracionDTO, proyectoExternalId, idPedido);
            return ResponseEntity.ok(colaboracion);
        }
        catch (RoleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

}
