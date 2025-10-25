package grupo16.dssd.api_cloud.controllers.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pedidos de colaboración", description = "Operaciones sobre pedidos de colaboración dentro de un proyecto")
@SecurityRequirement(name = "bearerAuth")
public class PedidoColaboracionControllerImpl implements I_PedidoColaboracionController {

    private final I_PedidoColaboracionService pedidoColaboracionService;
    private final UserService userService;
    private final I_ProyectoService proyectoService;


    @Override
    @PostMapping
    @Operation(
            summary = "Crear pedido de colaboración",
            description = "Crea un pedido de colaboración dentro del proyecto indicado. Requiere JWT (Authorization: Bearer <token>). " +
                    "Solo el usuario que creó el proyecto puede crear pedidos para ese proyecto.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto donde se crea el pedido", required = true, example = "42")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Payload del pedido de colaboración",
                    content = @Content(
                            schema = @Schema(implementation = PedidoColaboracionDTO.class),
                            examples = {
                                    @ExampleObject(name = "Ejemplo de pedido", value = """
                                            {
                                              "nombre": "USD 2000",
                                              "fechaInicio": "2025-10-20",
                                              "fechaFin": "2025-10-22",
                                              "recurso": "DINERO"
                                            }
                        """)
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido creado correctamente",
                            content = @Content(schema = @Schema(implementation = PedidoColaboracionDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o proyecto inexistente"),
            }
    )
    public ResponseEntity<?> crearPedido(HttpServletRequest request, @PathVariable Long idProyecto, @RequestBody PedidoColaboracionDTO pedidoDTO) {

        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }

        PedidoColaboracionDTO pedido = null;

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El id de proyecto indicado no existe."));

            if (!proyecto.getCargadoPor().equals(user)) {
                return ResponseEntity.status(403).body("No tienes permisos para crear un pedido en este proyecto.");
            }

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
    @Operation(
            summary = "Listar pedidos por proyecto",
            description = "Devuelve los pedidos de colaboración asociados al proyecto indicado. Requiere JWT.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", required = true, example = "42")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pedidos",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoColaboracionDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "ID inválido o proyecto no encontrado")
            }
    )
    public ResponseEntity<?> getByProyecto(HttpServletRequest request, @PathVariable Long idProyecto) {

        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }

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
