package grupo16.dssd_backend.services.bonita;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import grupo16.dssd_backend.helpers.NombresProcesos;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.HttpCookie;
import java.util.*;

@Service
class BonitaService implements I_BonitaService {

    private final RestClient client;
    private static final Logger logger = LoggerFactory.getLogger(BonitaService.class);
    private final ObjectMapper mapper;

    public BonitaService(@Value("${external.service.url}/bonita") String baseUrl, ObjectMapper mapper) {
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.mapper = mapper;
    }

    @Override
    public BonitaSession loginAndReturnCookies(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("password", password);
        form.add("redirect", "false");

        return client.post()
            .uri("/loginservice")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .exchange((req, resp) -> {
                // Tomamos Set-Cookie
                List<String> setCookies = resp.getHeaders().get(HttpHeaders.SET_COOKIE);
                if (setCookies == null || setCookies.isEmpty()) {
                    String body = resp.bodyTo(String.class);
                    throw new IllegalStateException("Login Bonita falló: sin Set-Cookie. Body: " + body);
                }

                Map<String, String> cookies = parseSetCookieHeaders(setCookies);
                String js = cookies.get("JSESSIONID");
                String xt = cookies.get("X-Bonita-API-Token");

                if (js == null || xt == null) {
                    throw new IllegalStateException(
                            "Login Bonita: faltan cookies JSESSIONID/X-Bonita-API-Token.");
                }

                return new BonitaSession(username, js, xt, System.currentTimeMillis(), null, null);
            });
    }

//    @Override
//    public void logout(BonitaSession session) {
//
//    }

    @Override
    public Long instanciarProcesoCreacionProyecto(Proyecto proyecto) {

        // Buscar proceso por nombre, obtener id
        Optional<String> resp = this.buscarProcesoPorNombre(NombresProcesos.PROCESO_CREAR_PROYECTO);
        if (resp.isEmpty()) {
            throw new IllegalStateException("No se encontró el proceso " + NombresProcesos.PROCESO_CREAR_PROYECTO);
        }

        Long id = Long.valueOf(resp.get());
        logger.info("PROCESO ENCONTRADO: "+ resp.get());

        // Instanciar proceso
        Map<String, Object> instancia = this.instanciarProceso(String.valueOf(id));

        String caseId = String.valueOf(instancia.get("caseId"));
        logger.info("CASE ID: "+ caseId);

        return Long.parseLong(caseId);
    }

    @Override
    public void ejecutarSiguienteTareaReady(Long caseId) {
        // Obtener tareas del caso
        List<Map<String, Object>> tareas =
                this.buscarTareasReadyPorCaso(caseId.toString());

        // Asignar tarea a usuario
        while (tareas.isEmpty()) {
            logger.info("No hay tareas ready en el caso " + caseId);
            tareas = this.buscarTareasReadyPorCaso(caseId.toString());
        }
        String taskId = String.valueOf(tareas.getFirst().get("id"));

        String userId = BonitaSessionHolder.getBonitaSession().userId().toString();

        this.asignarTareaAUsuario(taskId, userId);
        logger.info("TAREA ASIGNADA: "+ userId);

        // Ejecutar tarea
        this.ejecutarTareaDeUsuario(taskId, null);
    }

    @Override
    public BonitaSession getUserRole() {

        Map<String, String> session = client.get()
                .uri("/API/system/session/1")
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (session == null || session.isEmpty()) {
            throw new RuntimeException("No se pudo consultar la sesión del usuario");
        }

        String userId = session.get("user_id");

        logger.debug("Sesión del usuario consultada, user_id: "+userId);


        List<Map<String,String>> memberships = client.get()
                .uri(builder -> builder
                        .path("/API/identity/membership")
                        .queryParam("f", "user_id=" + userId)
                        .build())
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (memberships == null || memberships.isEmpty()) {
            throw new RuntimeException("No se pudo obtener membership del usuario");
        }

        String roleId = memberships.getFirst().get("role_id");

        logger.debug("Membership consultado, role_id: "+roleId);

        Map<String,String> role = client.get()
                .uri("/API/identity/role/" + roleId)
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (role == null || role.isEmpty()) {
            throw new RuntimeException("No se pudo consultar el rol");
        }

        String roleName = role.get("name");

        logger.debug("Role consultado, name: " + roleName);

        Role roleEnum = Role.fromValue(roleName);

        BonitaSession current = BonitaSessionHolder.getBonitaSession();

        return new BonitaSession(
                current.username(),
                current.jsessionId(),
                current.xBonitaToken(),
                current.createdAtEpochMs(),
                roleEnum,
                Integer.parseInt(userId)
        );
    }

    /*
     * Listado de caseId de procesos de proyectos iniciados por el usuario logueado
     *
     */
    @Override
    public List<Long> getUserProcessesCaseIds(String processName) {

        List<Map<String, Object>> procs = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/API/bpm/process")
                        .queryParam("f", "name=" + processName)
                        .queryParam("f", "activationState=ENABLED")
                        .build())
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (procs == null || procs.isEmpty()) return List.of();

        // si hay varias versiones, elegimos la mayor (podés cambiar a deploymentDate)
        var processId = procs.stream()
                .max(Comparator.comparing(m -> String.valueOf(m.get("version"))))
                .map(m -> String.valueOf(m.get("id")));

        BonitaSession bonitaSession = BonitaSessionHolder.getBonitaSession();

        List<Map<String, String>> cases = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/API/bpm/case")
                        .queryParam("f", "started_by=" + bonitaSession.userId())
                        .queryParam("processDefinitionId", processId.get())
                        .build())
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (cases == null || cases.isEmpty()) return List.of();

        return cases.stream()
                .map(c -> Long.parseLong(c.get("rootCaseId")))
                .toList();
    }


    private Optional<String> buscarProcesoPorNombre(String processName) {
        List<Map<String, Object>> procs = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/API/bpm/process")
                        .queryParam("p", "0")
                        .queryParam("c", "10")
                        .queryParam("f", "name=" + processName)
                        .queryParam("f", "activationState=ENABLED")
                        .build())
                .headers(this::withAuth)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (procs == null || procs.isEmpty()) return Optional.empty();

        // si hay varias versiones, elegimos la mayor (podés cambiar a deploymentDate)
        return procs.stream()
            .max(Comparator.comparing(m -> String.valueOf(m.get("version"))))
            .map(m -> String.valueOf(m.get("id")));
    }

    private Map<String, Object> instanciarProceso(String processId) {

        return client.post()
            .uri("/API/bpm/process/{id}/instantiation", processId)
            .headers(this::withAuth)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public List<Map<String, Object>> buscarTareasReadyPorCaso(String caseId) {
        return client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/API/bpm/humanTask")
                .queryParam("p", "0")
                .queryParam("c", "50")
                .queryParam("f", "state=ready")
                .queryParam("f", "caseId=" + caseId)
                .build())
            .headers(this::withAuth)
            .retrieve()
            .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    @Override
    public void setVariablesCase(String caseId, Map<String, Object> variables) {

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String varName = entry.getKey();
            Map<String, Object> body = crearBodyVariable(entry);

            client.put()
                    .uri("/API/bpm/caseVariable/{caseId}/{varName}", caseId, varName)
                    .headers(this::withAuth)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    private static Map<String, Object> crearBodyVariable(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();

        String type = switch (value) {
            case String s -> "java.lang.String";
            case Integer i -> "java.lang.Integer";
            case Long l -> "java.lang.Long";
            case Double v -> "java.lang.Double";
            case Boolean b -> "java.lang.Boolean";
            case null, default -> {
                assert value != null;
                throw new IllegalArgumentException("Tipo no soportado: " + value.getClass());
            }
        };

        Map<String, Object> body = Map.of(
                "type", type,
                "value", value.toString()
        );
        return body;
    }

    public void asignarTareaAUsuario(String taskId, String userId) {
        client.put()
            .uri("/API/bpm/humanTask/{id}", taskId)
            .headers(this::withAuth)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("assigned_id", userId))
            .retrieve()
            .toBodilessEntity();
    }


    public void ejecutarTareaDeUsuario(String taskId, Map<String, Object> contract) {
        client.post()
            .uri("/API/bpm/userTask/{id}/execution", taskId)
            .headers(this::withAuth)
            .contentType(MediaType.APPLICATION_JSON)
            .body(contract != null ? contract : Map.of())
            .retrieve()
            .toBodilessEntity();
    }

    private void withAuth(HttpHeaders headers) {

        BonitaSession bonitaSession = BonitaSessionHolder.getBonitaSession();
        String jSessionId = bonitaSession.jsessionId();
        String xBonitaToken = bonitaSession.xBonitaToken();

        logger.info("SESIÓN RECUPERADA: " + jSessionId + " " + xBonitaToken);

        if (jSessionId == null || xBonitaToken == null) {
            throw new IllegalStateException("No hay sesión Bonita. Llamá a login() primero.");
        }
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + jSessionId + "; X-Bonita-API-Token=" + xBonitaToken);
        // para POST/PUT/DELETE Bonita exige también el header X-Bonita-API-Token
        headers.add("X-Bonita-API-Token", xBonitaToken);
    }

    private static Map<String, String> parseSetCookieHeaders(List<String> setCookies) {
        // convierte múltiples Set-Cookie en un mapa nombre->valor
        Map<String, String> out = new HashMap<>();
        for (String sc : setCookies) {
            // HttpCookie.parse maneja atributos; tomamos el primero (nombre=valor)
            List<HttpCookie> parsed = HttpCookie.parse(sc);
            for (HttpCookie ck : parsed) {
                out.put(ck.getName(), ck.getValue());
            }
        }
        return out;
    }

    @Override
    public Object getCaseVariableValue(Long caseId, String variableName) {

        var json = client.get()
                .uri("/API/bpm/caseVariable/{caseId}/{var}", caseId, variableName)
                .headers(this::withAuth)
                .retrieve()
                .body(JsonNode.class);

        if (json == null) {
            throw new IllegalStateException("Fallo en request de variable a Bonita: " + variableName);
        }

        if (json.get("value").asText().equals("null")) {
            return null;
        }

        String type = json.get("type").asText();
        String rawValue = json.get("value").asText();

        return convertValue(type, rawValue);
    }

    private Object convertValue(String type, String raw) {

        // Strings vienen así: "\"hola\"" → hay que sacar comillas internas
        if (String.class.getName().equals(type)) {
            if (raw.startsWith("\"") && raw.endsWith("\""))
                raw = raw.substring(1, raw.length() - 1);
            return raw;
        }

        if ("java.lang.Long".equals(type)) {
            return Long.valueOf(raw);
        }

        if ("java.lang.Integer".equals(type)) {
            return Integer.valueOf(raw);
        }

        if ("java.lang.Boolean".equals(type)) {
            return Boolean.valueOf(raw);
        }

        // Podés agregar más tipos si usás objetos en Bonita
        return raw; // fallback
    }


}
