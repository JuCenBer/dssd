package grupo16.dssd.api_cloud.controllers.colaboracion;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface I_CollaborationRequestController {

    ResponseEntity<?> createRequest(@RequestBody PedidoColaboracionDTO request, String authorizationHeader);

    @PostMapping("/getByOrganizer")
    ResponseEntity<?> getByOrganize(PedidoColaboracionDTO request);
}
