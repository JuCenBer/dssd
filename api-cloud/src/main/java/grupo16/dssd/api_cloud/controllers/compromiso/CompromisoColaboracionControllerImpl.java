package grupo16.dssd.api_cloud.controllers.compromiso;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


    @Override
    public ResponseEntity<?> crearCompromiso(HttpServletRequest request, @PathVariable Long idProyecto, @PathVariable Long idPedido) {

        CompromisoColaboracionDTO compromisoDTO = null;

        // MÁS VALIDACIONES (y/o poner restricciones en el mapeo del modelo) para que no acepte cosas en null

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

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
