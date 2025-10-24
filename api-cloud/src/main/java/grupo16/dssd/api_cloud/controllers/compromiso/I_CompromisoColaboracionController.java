package grupo16.dssd.api_cloud.controllers.compromiso;

import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface I_CompromisoColaboracionController {

    ResponseEntity<?> crearCompromiso(HttpServletRequest request, Long idProyecto, Long idPedido, CompromisoColaboracionDTO compromisoDTO);
}
