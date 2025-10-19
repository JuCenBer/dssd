package grupo16.dssd.api_cloud.controllers.proyect;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface I_ProyectoController {

    public ResponseEntity<?> create(@RequestBody ProyectoDTO projectDTO);
}