package grupo16.dssd.api_cloud.controllers.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface I_PedidoColaboracionController {

    ResponseEntity<?> crearPedido(HttpServletRequest request, Long idProyecto, PedidoColaboracionDTO pedidoDTO);

    ResponseEntity<?> getByProyecto(HttpServletRequest request, Long idProyecto);

}
