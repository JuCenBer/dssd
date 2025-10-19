package grupo16.dssd.api_cloud.controllers.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface I_PedidoColaboracionController {

    ResponseEntity<?> crearPedido(@RequestBody PedidoColaboracionDTO request, String authorizationHeader);

    @PostMapping("/getByOrganizer")
    ResponseEntity<?> getByOrganizer(String usernameOrganizador, @RequestHeader(value = "Authorization") String authorizationHeader);
}
