package grupo16.dssd.api_cloud.controllers.proyecto;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface I_ProyectoController {

    ResponseEntity<?> create(HttpServletRequest request, ProyectoDTO proyectoDTO);

    ResponseEntity<?> getAll(HttpServletRequest request);

    ResponseEntity<?> get(HttpServletRequest request, Long idProyecto);
}