package grupo16.dssd.api_cloud.controllers.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyectos/{idProyecto}/pedidos")
@RequiredArgsConstructor
public class PedidoColaboracionControllerImpl implements I_PedidoColaboracionController {

    private final I_PedidoColaboracionService pedidoColaboracionService;
    private final UserService userService;
    private final I_ProyectoService proyectoService;


    @Override
    @PostMapping
    public ResponseEntity<?> crearPedido(HttpServletRequest request, @PathVariable Long idProyecto, @RequestBody PedidoColaboracionDTO pedidoDTO) {

        PedidoColaboracionDTO pedido = null;

        // MÁS VALIDACIONES (y/o poner restricciones en el mapeo del modelo) para que no acepte cosas en null

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            pedido = this.pedidoColaboracionService.crearPedidoColaboracion(pedidoDTO, user, proyecto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(":c Ha ocurrido un problema: " + e.getMessage());
        }
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(pedido.getId())
                        .toUri()).body(pedido);
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getByProyecto(HttpServletRequest request, @PathVariable Long idProyecto) {

        List<PedidoColaboracionDTO> pedidos = null;

        try {
            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new EmptyResultDataAccessException(1));

            pedidos = PedidoColaboracionDTO.fromEntity(proyecto.getPedidosColaboracion(), Boolean.FALSE);

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("No se encontró el proyecto.");
        }

        return ResponseEntity.ok(pedidos);
    }

}
