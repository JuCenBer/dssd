package grupo16.dssd.api_cloud.controllers.proyecto;

import grupo16.dssd.api_cloud.dtos.ObservacionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoCompletoDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.dtos.bonita.CreacionProyectoDTO;
import grupo16.dssd.api_cloud.models.EstadoProyecto;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.Role;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.services.pedido.I_PedidoColaboracionService;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.users.UserService;
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
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/proyectos")
@RequiredArgsConstructor
@Tag(name = "Proyectos", description = "Operaciones para crear y consultar proyectos")
@SecurityRequirement(name = "bearerAuth")
public class ProyectoControllerImpl {

    private final I_ProyectoService proyectoService;
    private final UserService userService;
    private final I_PedidoColaboracionService pedidosService;

    @PostMapping
    @Operation(
            summary = "Crear proyecto",
            description = "Crea un nuevo proyecto. Requiere un token JWT válido en el header `Authorization: Bearer <token>`.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del nuevo proyecto",
                    content = @Content(
                            schema = @Schema(implementation = ProyectoDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de creación",
                                            value = """
                                                    {
                                                      "nombre": "Proyecto Techando",
                                                      "caseId": "12345678",
                                                      "descripcion": "Descripcion del proyecto",
                                                      "ubicacion": "La Plata, Bs As, Argentina"
                                                    }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Proyecto creado correctamente",
                            content = @Content(schema = @Schema(implementation = ProyectoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado o token inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno")
            }
    )
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ProyectoDTO proyectoDTO){

        try {
            User cargadoPor = this.userService.getUserByUsername((String) request.getAttribute("username"));
            proyectoDTO = this.proyectoService.crearProyecto(proyectoDTO, cargadoPor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(proyectoDTO);
    }

    @GetMapping
    @Operation(
            summary = "Listar proyectos",
            description = "Devuelve todos los proyectos visibles para el usuario autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de proyectos",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProyectoDTO.class)))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autorizado o token inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno")
            }
    )
    public ResponseEntity<?> getAll(HttpServletRequest request) {

        return ResponseEntity.ok(this.proyectoService.findAll());
    }

    @GetMapping("/{idProyecto}")
    @Operation(
            summary = "Obtener proyecto por ID",
            description = "Obtiene un proyecto por su identificador. Requiere autenticación JWT.",
            parameters = {
                    @Parameter(name = "idProyecto", description = "ID del proyecto", example = "42", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Proyecto encontrado",
                            content = @Content(schema = @Schema(implementation = ProyectoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "ID inválido o proyecto no encontrado"),
                    @ApiResponse(responseCode = "401", description = "No autorizado o token inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno")
            }
    )
    public ResponseEntity<?> get(HttpServletRequest request, @PathVariable Long idProyecto, @RequestParam(required = false, defaultValue = "false") Boolean completo) {

        if (idProyecto == null || idProyecto < 1) {
            return ResponseEntity.badRequest().body("ID de proyecto inválido.");
        }

        Proyecto proyecto;
        try {
            proyecto = this.proyectoService.findById(idProyecto)
                    .orElseThrow(() -> new Exception("No se encontró el proyecto"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if (completo) {
            return ResponseEntity.ok(ProyectoCompletoDTO.fromEntity(proyecto));
        } else {
            return ResponseEntity.ok(ProyectoDTO.fromEntity(proyecto));
        }
    }

    @PostMapping("/bonita")
    public ResponseEntity<?> createFromBonita(HttpServletRequest request, @RequestBody CreacionProyectoDTO creacionProyectoDTO){

        try {
            User cargadoPor = this.userService.getUserByUsername((String) request.getAttribute("username"));
            ProyectoDTO proyectoDTO = this.proyectoService.crearProyecto(creacionProyectoDTO, cargadoPor);

            Proyecto proyecto = this.proyectoService.findById(proyectoDTO.getId())
                    .orElseThrow(() -> new IllegalStateException("No se pudo crear el proyecto"));

            this.pedidosService.crearPedidosColaboracion(creacionProyectoDTO.actividades(), proyecto);

            return ResponseEntity.ok(proyectoDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar")
    ResponseEntity<?> finalizarProceso(HttpServletRequest request, @PathVariable Long id) {

        try {
            Proyecto proyecto = this.proyectoService.findById(id)
                    .orElseThrow();

            if (!proyecto.getCargadoPor().getUsername().equals(request.getAttribute("username"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tiene permiso para actualizar este proyecto");
            }
            if (!proyecto.getEstado().equals(EstadoProyecto.EN_EJECUCION)) {
                return ResponseEntity.badRequest().body("Sólo los proyectos en ejecución pueden ser finalizados");
            }

            this.proyectoService.updateEstado(proyecto, EstadoProyecto.FINALIZADO);

            return ResponseEntity.ok().build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/{id}/observacion")
    ResponseEntity<?> crearObservacion(HttpServletRequest request, @PathVariable Long id, @RequestBody ObservacionDTO observacionDTO) {

        try {
            Proyecto proyecto = this.proyectoService.findById(id)
                    .orElseThrow();

            User user = this.userService.getUserByUsername((String)request.getAttribute("username"));

            if (!user.getRole().equals(Role.DIRECTIVO)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "No tiene permiso para realizar esta acción."));
            }
            if (!proyecto.getEstado().equals(EstadoProyecto.EN_EJECUCION)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Sólo se pueden agregar observaciones a proyectos en ejecución"));
            }

            ObservacionDTO observacion = this.proyectoService.agregarObservacion(proyecto, observacionDTO, user);

            return ResponseEntity.ok(observacion);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/{id}/observacion/{idObservacion}/resolver")
    ResponseEntity<?> resolverObservacion(HttpServletRequest request, @PathVariable Long id, @PathVariable Long idObservacion) {

        try {
            Proyecto proyecto = this.proyectoService.findById(id)
                    .orElseThrow();

            User user = this.userService.getUserByUsername((String)request.getAttribute("username"));

            if (!user.getRole().equals(Role.ONG_SOL)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "No tiene permiso para realizar esta acción."));
            }
            if (!proyecto.getEstado().equals(EstadoProyecto.EN_EJECUCION)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Sólo se pueden resolver observaciones en proyectos en ejecución"));
            }

            if (!proyecto.getCargadoPor().getUsername().equals(user.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Sólo se pueden resolver observaciones en proyectos propios"));
            }

            ProyectoCompletoDTO proyectoDTO = this.proyectoService.resolverObservacion(proyecto, idObservacion);

            return ResponseEntity.ok(proyectoDTO);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}