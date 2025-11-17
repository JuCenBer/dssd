package grupo16.dssd.api_cloud.controllers.compromiso;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.compromiso.I_CompromisoColaboracionService;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
import grupo16.dssd.api_cloud.utils.JwtUtils;
import io.swagger.v3.core.util.Json;
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
import org.apache.coyote.Response;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.spi.CollatorProvider;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyectos/{idProyecto}/pedidos/{idPedido}/compromisos")
@RequiredArgsConstructor
@Tag(name = "Compromisos de colaboración", description = "Operaciones sobre compromisos dentro de un pedido de colaboración")
@SecurityRequirement(name = "bearerAuth")
public class CompromisoColaboracionControllerImpl implements I_CompromisoColaboracionController {

    private final I_ProyectoService proyectoService;
    private final I_PedidoColaboracionService pedidoColaboracionService;
    private final I_CompromisoColaboracionService compromisoColaboracionService;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    @Override
    @PostMapping
    @Operation(
            summary = "Crear compromiso de colaboración",
            description = "Crea un compromiso asociado a un pedido dentro de un proyecto. Requiere JWT. " +
                    "El usuario autenticado no puede crear compromisos en su propio proyecto (ONG).",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", required = true, example = "42"),
                    @Parameter(name = "idPedido", description = "ID del pedido dentro del proyecto", required = true, example = "7")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del compromiso a crear",
                    content = @Content(
                            schema = @Schema(implementation = CompromisoColaboracionDTO.class),
                            examples = {
                                    @ExampleObject(name = "Ejemplo crear compromiso", value = """
                                            {
                                              "descripcion":"Ayuda económica"
                                            }
                                            
                        """)
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Compromiso creado correctamente", content = @Content(schema = @Schema(implementation = CompromisoColaboracionDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o recurso no encontrado"),
            }
    )
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
                    .orElseThrow(() -> new Exception("El proyecto indicado no existe."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("El pedido indicado no existe."));

            if (!pedido.getProyectoPedido().getId().equals(proyecto.getId())) {
                return ResponseEntity.badRequest().body("El pedido no pertenece al proyecto indicado.");
            }

            if (proyecto.getCargadoPor().equals(user)) {
                return ResponseEntity.badRequest().body("No puedes cargar un compromiso a un proyecto de tu misma ONG.");
            }


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

    @GetMapping
    @Operation(
            summary = "Listar compromisos de un pedido",
            description = "Devuelve los compromisos asociados a un pedido dentro del proyecto indicado. Requiere JWT.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", required = true, example = "42"),
                    @Parameter(name = "idPedido", description = "ID del pedido", required = true, example = "7")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de compromisos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompromisoColaboracionDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "ID inválido o recurso no encontrado"),
            }
    )
    public ResponseEntity<?> getAll(@PathVariable Long idProyecto, @PathVariable Long idPedido){
        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }
        if (idPedido == null || idPedido < 1) {
            return ResponseEntity.badRequest().body("ID de pedido inválido.");
        }

        List<CompromisoColaboracionDTO> compromisos = null;

        try {
            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new RuntimeException("No se encontró el proyecto indicado."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idPedido)
                    .orElseThrow(() -> new RuntimeException("No se encontró el pedido indicado."));

            if (!pedido.getProyectoPedido().getId().equals(proyecto.getId())) {
                return ResponseEntity.badRequest().body("El pedido no pertenece al proyecto indicado.");
            }

            compromisos =  pedido.getCompromisosColaboracion().stream()
                    .map(compromiso -> CompromisoColaboracionDTO.fromEntity(compromiso, false)).toList();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(compromisos);
    }

    @Override
    @GetMapping("/{idCompromiso}")
    @Operation(
            summary = "Obtener un compromiso",
            description = "Obtiene un compromiso por su id dentro del pedido y proyecto indicados. Requiere JWT.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", required = true, example = "42"),
                    @Parameter(name = "idPedido", description = "ID del pedido", required = true, example = "7"),
                    @Parameter(name = "idCompromiso", description = "ID del compromiso", required = true, example = "100")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compromiso encontrado", content = @Content(schema = @Schema(implementation = CompromisoColaboracionDTO.class))),
                    @ApiResponse(responseCode = "400", description = "ID inválido o recurso no encontrado"),
            }
    )
    public ResponseEntity<?> get(@PathVariable Long idProyecto, @PathVariable Long idPedido, @PathVariable Long idCompromiso) {

        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }
        if (idPedido == null || idPedido < 1) {
            return ResponseEntity.badRequest().body("ID de pedido inválido.");
        }
        if (idCompromiso == null || idCompromiso < 1) {
            return ResponseEntity.badRequest().body("ID de compromiso inválido.");
        }

        CompromisoColaboracion compromiso = null;

        try {
            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new RuntimeException("No se encontró el proyecto indicado."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idPedido)
                    .orElseThrow(() -> new RuntimeException("No se encontró el pedido indicado."));

            if (!pedido.getProyectoPedido().getId().equals(proyecto.getId())) {
                return ResponseEntity.badRequest().body("El pedido no pertenece al proyecto indicado.");
            }

            compromiso = this.compromisoColaboracionService.findById(idCompromiso)
                    .orElseThrow(() -> new RuntimeException("No se encontró el compromiso indicado."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity
                .ok().body(CompromisoColaboracionDTO.fromEntity(compromiso, Boolean.TRUE));
    }

    @Override
    @PostMapping("/{idCompromiso}/cumplir")
    @Operation(
            summary = "Marcar compromiso como cumplido",
            description = "Marca un compromiso como cumplido. Solo el propietario del proyecto (ONG) puede marcarlo como cumplido. Requiere JWT.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", required = true, example = "42"),
                    @Parameter(name = "idPedido", description = "ID del pedido", required = true, example = "7"),
                    @Parameter(name = "idCompromiso", description = "ID del compromiso a cumplimentar", required = true, example = "100")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compromiso marcado como cumplido", content = @Content(schema = @Schema(implementation = CompromisoColaboracionDTO.class))),
                    @ApiResponse(responseCode = "400", description = "ID inválido o precondición no cumplida"),
            }
    )
    public ResponseEntity<?> cumplirCompromiso(HttpServletRequest request, @PathVariable Long idProyecto, @PathVariable Long idPedido, @PathVariable Long idCompromiso) {

        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }
        if (idPedido == null || idPedido < 1) {
            return ResponseEntity.badRequest().body("ID de pedido inválido.");
        }
        if (idCompromiso == null || idCompromiso < 1) {
            return ResponseEntity.badRequest().body("ID de compromiso inválido.");
        }

        CompromisoColaboracion compromiso = null;
        CompromisoColaboracionDTO compromisoDTO = null;

        try {
            User user = this.userService.getUserByUsername((String) request.getAttribute("username"));

            Proyecto proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new RuntimeException("No se encontró el proyecto indicado."));

            PedidoColaboracion pedido = this.pedidoColaboracionService.findById(idPedido)
                    .orElseThrow(() -> new RuntimeException("No se encontró el pedido indicado."));

            if (!pedido.getProyectoPedido().getId().equals(proyecto.getId())) {
                return ResponseEntity.badRequest().body("El pedido no pertenece al proyecto indicado.");
            }

            compromiso = this.compromisoColaboracionService.findById(idCompromiso)
                    .orElseThrow(() -> new RuntimeException("No se encontró el compromiso indicado."));

            if (!proyecto.getCargadoPor().getNombreOng().equals(user.getNombreOng())) {
                return ResponseEntity.badRequest().body("No puedes marcar como cumplido un compromiso de un proyecto que no es de tu ONG.");
            }

            compromisoDTO = this.compromisoColaboracionService.cumplirCompromiso(compromiso);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity
                .ok().body(compromisoDTO);
    }
}
