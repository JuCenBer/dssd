package grupo16.dssd.api_cloud.controllers.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.colaboracion.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoColaboracionControllerImpl implements I_PedidoColaboracionController {

    private final I_PedidoColaboracionService pedidoColaboracionService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final I_ProyectoService proyectoService;


    @Override
    @PostMapping
    public ResponseEntity<?> crearPedido(HttpServletRequest request, @RequestBody PedidoColaboracionDTO pedidoDTO) {
        PedidoColaboracionDTO pedido = null;

        if (pedidoDTO.getProyectoPedido() == null || pedidoDTO.getProyectoPedido().getId() == null) {
            return ResponseEntity.badRequest().body("Se debe especificar el ID del proyecto");
        }

        // MÁS VALIDACIONES (y/o poner restricciones en el mapeo del modelo) para que no acepte cosas en null

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(pedidoDTO.getProyectoPedido().getId())
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            pedido = this.pedidoColaboracionService.crearPedidoColaboracion(pedidoDTO, user, proyecto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(":c Ha ocurrido un problema: " + e.getMessage());
        }
        return ResponseEntity.ok(pedido);
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {

        // ESTARÍA BUENO PAGINADO

        return ResponseEntity.ok(this.pedidoColaboracionService.findAll());
    }

}
