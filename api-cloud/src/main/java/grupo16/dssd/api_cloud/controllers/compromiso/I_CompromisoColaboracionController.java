package grupo16.dssd.api_cloud.controllers.compromiso;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface I_CompromisoColaboracionController {

    ResponseEntity<?> crearCompromiso(HttpServletRequest request, Long idProyecto, Long idPedido);
}
