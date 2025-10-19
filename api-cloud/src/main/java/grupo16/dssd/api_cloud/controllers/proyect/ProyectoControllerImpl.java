package grupo16.dssd.api_cloud.controllers.proyect;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyecto")
@RequiredArgsConstructor
public class ProyectoControllerImpl implements I_ProyectoController {

    private final I_ProyectoService proyectoService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

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
}