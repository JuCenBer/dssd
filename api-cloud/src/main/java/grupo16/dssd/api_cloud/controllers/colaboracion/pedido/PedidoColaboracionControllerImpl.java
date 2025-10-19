package grupo16.dssd.api_cloud.controllers.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.colaboracion.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
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
    @PostMapping("/create")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoColaboracionDTO pedidoDTO, @RequestHeader(value = "Authorization") String authorizationHeader) {
        PedidoColaboracion pedido = null;
        try {
            User user = this.userService.getUserByUsername(this.jwtUtils.extractUsername(authorizationHeader));

            Proyecto proyecto = this.proyectoService.findById(pedidoDTO.getId())
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            pedido = this.pedidoColaboracionService.crearPedidoColaboracion(pedidoDTO, user, proyecto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(":c Ha ocurrido un problema: " + e.getMessage());
        }
        return ResponseEntity.ok(pedido);
    }

    @Override
    @PostMapping("/getByOrganizer")
    public ResponseEntity<?> getByOrganizer(String usernameOrganizador, @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = null;
        List<PedidoColaboracionDTO> pedidosColaboracion = null;
        try {
            user = this.userService.getUserByUsername(this.jwtUtils.extractUsername(authorizationHeader));
            User userOrganizador = this.userService.getUserByUsername(usernameOrganizador);
            pedidosColaboracion = this.pedidoColaboracionService.getPedidoColaboracionByUsuarioOrganizador(userOrganizador);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(":c Ha ocurrido un problema: " + e.getMessage());
        }
        return ResponseEntity.ok("Colaboracion creada. " + pedidosColaboracion.toString());
    }
}
