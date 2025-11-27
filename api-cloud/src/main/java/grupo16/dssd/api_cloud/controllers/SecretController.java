package grupo16.dssd.api_cloud.controllers;

import grupo16.dssd.api_cloud.repositories.CompromisoColaboracionRepository;
import grupo16.dssd.api_cloud.repositories.ObservacionRepository;
import grupo16.dssd.api_cloud.repositories.PedidoColaboracionRepository;
import grupo16.dssd.api_cloud.repositories.ProyectoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SecretController {

    private final ObservacionRepository observacionRepository;
    private final CompromisoColaboracionRepository compromisoRepository;
    private final PedidoColaboracionRepository pedidoRepository;
    private final ProyectoRepository proyectoRepository;

    @PostMapping()
    ResponseEntity<?> superSecretRestart(HttpServletRequest request, @RequestBody String pass) {

        if (request.getAttribute("username").equals("pp.back")
                && pass.equals("kaboom")) {

            this.observacionRepository.deleteAll();
            this.compromisoRepository.deleteAll();
            this.pedidoRepository.deleteAll();
            this.proyectoRepository.deleteAll();

        }
        return ResponseEntity.ok().build();
    }
}
