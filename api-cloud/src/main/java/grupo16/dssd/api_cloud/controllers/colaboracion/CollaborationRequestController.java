package grupo16.dssd.api_cloud.controllers.colaboracion;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.requests.CollaborationRequestService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/collaboration_request")
@RequiredArgsConstructor
public class CollaborationRequestController implements I_CollaborationRequestController {

    private final CollaborationRequestService collaborationRequestService;
    private final UserService userService;
    private final JwtUtils jwtUtils;


    @Override
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody PedidoColaboracionDTO request, @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = null;
        try {
            user = this.userService.getUserByUsername(this.jwtUtils.extractUsername(authorizationHeader));
            this.collaborationRequestService.createCollaborationRequest(request, user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Ha ocurrido un problema :c " + e.getMessage());
        }
        return ResponseEntity.ok("Colaboracion creada.");
    }

    @Override
    @PostMapping("/getByOrganizer")
    public ResponseEntity<?> getByOrganize(String usernameOrganizador, @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = null;
        List<PedidoColaboracionDTO> pedidosColaboracion = null;
        try {
            user = this.userService.getUserByUsername(this.jwtUtils.extractUsername(authorizationHeader));
            pedidosColaboracion = this.collaborationRequestService.getCollaborationRequestByOrganizer(usernameOrganizador);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Ha ocurrido un problema :c " + e.getMessage());
        }
        return ResponseEntity.ok("Colaboracion creada. " + pedidosColaboracion.toString());
    }
}
