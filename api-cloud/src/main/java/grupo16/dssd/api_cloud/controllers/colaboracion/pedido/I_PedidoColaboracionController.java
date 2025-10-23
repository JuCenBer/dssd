package grupo16.dssd.api_cloud.controllers.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface I_PedidoColaboracionController {

    ResponseEntity<?> crearPedido(HttpServletRequest request, PedidoColaboracionDTO pedidoDTO);

    ResponseEntity<?> getByProyecto(HttpServletRequest request, Long proyectoId);

}
