package grupo16.dssd_backend.controllers.proyectos;

import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import grupo16.dssd_backend.models.Role;
import grupo16.dssd_backend.services.proyecto.I_ProyectoService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/proyectos")
public class ProyectoControllerV1 implements I_ProyectoController {

    private final List<I_ProyectoService> proyectoServices;

    private Map<Role, I_ProyectoService> proyectoServiceMap;

    public ProyectoControllerV1(List<I_ProyectoService> proyectoServices) {
        this.proyectoServices = proyectoServices;
    }

    @PostConstruct
    private void init() {
        this.proyectoServiceMap = new HashMap<>();

        for (I_ProyectoService service : this.proyectoServices) {
            this.proyectoServiceMap.put(service.roleForService(), service);
        }
    }

    private I_ProyectoService getCorrectProyectoService() {
        BonitaSession session = BonitaSessionHolder.getBonitaSession();

        return this.proyectoServiceMap.get(BonitaSessionHolder.getBonitaSession().role());
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getProyectos() {

        try {
            List<ProyectoDTO> proyectos = this.getCorrectProyectoService().getProyectos();

            if (proyectos.isEmpty()) return ResponseEntity.noContent().build();

            return ResponseEntity.ok(proyectos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }



    }

    @Override
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoDTO proyectoDTO) {

        try {
            this.getCorrectProyectoService().createProject(proyectoDTO);
        } catch (RoleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("message", "Proyecto creado exitosamente"));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getProyecto(@PathVariable("id") Long proyectoId) {

        try {
            return ResponseEntity.ok(this.getCorrectProyectoService().getProyecto(proyectoId));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        }
    }
}
