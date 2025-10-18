package grupo16.dssd.api_cloud.controllers.colaboracion;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.requests.CollaborationRequestService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No se encontro el usuario");
        }
        this.collaborationRequestService.createCollaborationRequest(request, user);
        return ResponseEntity.ok("Colaboracion creada.");
    }

    @Override
    @PostMapping("/getByOrganizer")
    public ResponseEntity<?> getByOrganize(PedidoColaboracionDTO request) {
        return null;
    }
}
