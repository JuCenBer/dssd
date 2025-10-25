package grupo16.dssd.api_cloud.controllers.auth;

import grupo16.dssd.api_cloud.dtos.AuthResponse;
import grupo16.dssd.api_cloud.dtos.LoginRequest;
import grupo16.dssd.api_cloud.dtos.RegisterRequest;
import grupo16.dssd.api_cloud.services.auth.AuthService;
import grupo16.dssd.api_cloud.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Endpoints para login y registro de usuarios"
)
public class AuthControllerV1 implements I_AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Override
    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica un usuario mediante su `username` y `apiKey`, devolviendo un token JWT si las credenciales son válidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credenciales de acceso",
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                    {
                      "username": "usuario123",
                      "apiKey": "mi-clave-api"
                    }
                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticación exitosa, devuelve el token JWT",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas"
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = null;

        try {
            token = this.authService.authenticate(request.username(), request.apiKey());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Override
    @PostMapping("/register")
    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario u organización con su `username`, `nombreOng` y `apiKey`.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del nuevo usuario",
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(value = """
                    {
                      "username": "usuario123",
                      "nombreOng": "Mi ONG Solidaria",
                      "apiKey": "mi-clave-api"
                    }
                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario registrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"User registered successfully\"")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno o usuario ya existente"
                    )
            }
    )
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            this.userService.register(
                    request.username(),
                    request.nombreOng(),
                    request.apiKey()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Algo se rompió :c "+ e.getMessage());
        }

        return ResponseEntity.ok("User registered successfully");
    }
}
