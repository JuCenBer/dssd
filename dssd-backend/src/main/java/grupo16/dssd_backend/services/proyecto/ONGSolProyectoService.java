package grupo16.dssd_backend.services.proyecto;

//import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import grupo16.dssd_backend.dtos.ActividadDTO;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.helpers.NombresProcesos;
import grupo16.dssd_backend.models.Actividad;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ONGSolProyectoService extends AbstractProyectoService {


    @Override
    @Transactional
    public ProyectoDTO createProject(ProyectoDTO proyectoDTO) throws ValidationException {

        if(!proyectoDTO.isValid()){
            throw new ValidationException("Datos ingresados inválidos.");
        }
        Proyecto newProyecto = new Proyecto(proyectoDTO);

        if (newProyecto.getActividades().stream().noneMatch(Actividad::getRequiereColaboracion)) {
            throw new ValidationException("Al menos una actividad del proyecto debe requerir colaboración.");
        };

        // Bonita: instanciar proceso
        Long caseId = this.bonitaService.instanciarProcesoCreacionProyecto(newProyecto);
        newProyecto.setCaseId(caseId);

        // Guardar el id del usuario que cargó el proyecto
        //Integer bonitaUserId = BonitaSessionHolder.getBonitaSession().userId();
        //newProyecto.setCargadoPorId(bonitaUserId.longValue());

        // Persiste proyecto
        newProyecto = this.proyectoRepository.save(newProyecto);

        // Bonita: Setear variable de proceso proyectoJson
        String proyectoJson = null;
        try {
            proyectoJson = mapper.writeValueAsString(newProyecto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.bonitaService.setVariablesCase(
                caseId.toString(), Map.of(
                        "nombre", newProyecto.getNombre(),
                        "proyectoJson", proyectoJson
                )
        );

        // Bonita: Avanzar en tarea
        this.bonitaService.ejecutarSiguienteTareaReady(caseId);

        // Bonita: Obtener externalId
        Long externalId = null;
        while (externalId == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            externalId = (Long) this.bonitaService.getCaseVariableValue(caseId, "externalId");
        }

        // Guardar el externalId en el proyecto
        newProyecto.setExternalId(externalId);
        this.proyectoRepository.save(newProyecto);

        return ProyectoDTO.fromEntity(newProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> getProyectos() {
        List<Long> userCaseIds = this.bonitaService.getUserProcessesCaseIds(NombresProcesos.PROCESO_CREAR_PROYECTO);

        return ProyectoDTO.fromEntity(this.proyectoRepository.findByCaseIdIn(userCaseIds));
    }

    @Override
    public Role roleForService() {
        return Role.ONG_SOL;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public ProyectoDTO getProyecto(Long proyectoId) throws ValidationException {
//        ProyectoDTO proyectoDTO =  ProyectoDTO.fromEntity(
//                this.proyectoRepository.findById(proyectoId)
//                        .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"))
//        );
//
//        List<Long> userCaseIds = this.bonitaService.getUserProcessesCaseIds(NombresProcesos.PROCESO_CREAR_PROYECTO);
//
//        if (!userCaseIds.contains(proyectoDTO.caseId())) {
//            throw new ValidationException("No tienes permiso para visualizar este proyecto.");
//        }
//
//        // bonitaService -> Obtener compromisos de colaboración
//
//        return proyectoDTO;
//    }
}
