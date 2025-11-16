package grupo16.dssd_backend.controllers.auth;

import grupo16.dssd_backend.dtos.LoginDTO;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface I_AuthController {

    ResponseEntity<?> login(LoginDTO req, HttpServletRequest httpReq);

    ResponseEntity<?> logout(HttpServletRequest req);


}
