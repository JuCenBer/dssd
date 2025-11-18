package grupo16.dssd_backend.controllers.auth;

import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.dtos.LoginDTO;
import grupo16.dssd_backend.services.bonita.I_BonitaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
class AuthControllerV1 implements I_AuthController {

    private final I_BonitaService bonitaService;

    public AuthControllerV1(I_BonitaService bonitaService) {
        this.bonitaService = bonitaService;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO req, HttpServletRequest httpReq) {

        try {
            BonitaSession bonitaSession = this.bonitaService.loginAndReturnCookies(req.username(), req.password());
            // guardar en sesión
            var session = httpReq.getSession(true);
            session.setAttribute("bonitaSession", bonitaSession);

            bonitaSession = this.bonitaService.getUserRole();
            session.setAttribute("bonitaSession", bonitaSession);

            return ResponseEntity.ok().body(Map.of(
                    "username", bonitaSession.username(),
                    "role", bonitaSession.role(),
                    "message", "Sesión iniciada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            // opcional: llamar a /bonita/logoutservice con las cookies actuales
            var bs = (BonitaSession) request.getSession(false).getAttribute("bonitaSession");
            if (bs != null) bonitaService.logout(bs);
        } finally {
            if (request.getSession(false) != null) request.getSession(false).invalidate();
        }
        return ResponseEntity.noContent().build();
    }

}

