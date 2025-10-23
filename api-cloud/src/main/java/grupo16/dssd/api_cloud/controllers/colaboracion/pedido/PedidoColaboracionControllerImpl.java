package grupo16.dssd.api_cloud.controllers.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.colaboracion.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
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
    @GetMapping("/{proyectoId}")
    public ResponseEntity<?> getByProyecto(HttpServletRequest request, @PathVariable Long proyectoId) {

//        int page = Integer.parseInt(request.getParameter("page"));
//        if (page < 1){
//            return ResponseEntity.badRequest().body("La pagina debe ser un entero positivo");
//        }
//        int size = 10;
        List<PedidoColaboracionDTO> pedidos = null;

        try {
            Proyecto proyecto = this.proyectoService.findById(proyectoId)
                    .orElseThrow(() -> new EmptyResultDataAccessException(1));

            pedidos = PedidoColaboracionDTO.fromEntity(proyecto.getPedidosColaboracion(), Boolean.FALSE);

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("No se encontró el proyecto.");
        }

        return ResponseEntity.ok(pedidos);
    }

}
