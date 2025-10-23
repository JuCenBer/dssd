package grupo16.dssd.api_cloud.controllers.proyecto;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyectos")
@RequiredArgsConstructor
public class ProyectoControllerImpl implements I_ProyectoController {

    private final I_ProyectoService proyectoService;
    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ProyectoDTO proyectoDTO){

        try {
            User cargadoPor = this.userService.getUserByUsername((String) request.getAttribute("username"));
            proyectoDTO = this.proyectoService.crearProyecto(proyectoDTO, cargadoPor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(proyectoDTO);
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {

        return ResponseEntity.ok(this.proyectoService.findAll());
    }

    @Override
    @GetMapping("/{idProyecto}")
    public ResponseEntity<?> get(HttpServletRequest request, @PathVariable Long idProyecto) {

        Proyecto proyecto;
        try {
            proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("No se encontró el proyecto"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(ProyectoDTO.fromEntity(proyecto));
    }


}