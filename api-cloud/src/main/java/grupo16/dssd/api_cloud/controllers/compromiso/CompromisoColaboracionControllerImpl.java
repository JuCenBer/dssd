package grupo16.dssd.api_cloud.controllers.compromiso;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.compromiso.I_CompromisoColaboracionService;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyectos/{idProyecto}/pedidos/{idPedido}/compromisos")
@RequiredArgsConstructor
public class CompromisoColaboracionControllerImpl implements I_CompromisoColaboracionController {

    private final I_ProyectoService proyectoService;
    private final I_PedidoColaboracionService pedidoColaboracionService;
    private final I_CompromisoColaboracionService compromisoColaboracionService;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    @Override
    public ResponseEntity<?> crearCompromiso(HttpServletRequest request, @PathVariable Long idProyecto, @PathVariable Long idPedido, @RequestBody CompromisoColaboracionDTO compromisoDTO) {

        if(idProyecto == null || idProyecto < 1){
            ResponseEntity.badRequest().body("ID de proyecto invalido");
        }
        if(idPedido == null || idPedido < 1){
            ResponseEntity.badRequest().body("ID de pedido invalido");
        }
        if(compromisoDTO == null){
            ResponseEntity.badRequest().body("Compromiso invalido");
        }

        // MÁS VALIDACIONES (y/o poner restricciones en el mapeo del modelo) para que no acepte cosas en null

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            compromisoDTO.setPedidoColaboracion(PedidoColaboracionDTO.fromEntity(pedido, true));

            compromisoDTO = this.compromisoColaboracionService.crearCompromisoColaboracion(compromisoDTO, user, pedido);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(":c Ha ocurrido un problema: " + e.getMessage());
        }
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(compromisoDTO.getId())
                        .toUri()).body(compromisoDTO);
    }
}
