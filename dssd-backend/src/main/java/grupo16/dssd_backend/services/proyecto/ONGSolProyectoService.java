package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.helpers.NombresProcesos;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ONGSolProyectoService extends AbstractProyectoService {

    @Override
    public void createProject(ProyectoDTO proyectoDTO) throws ValidationException {

        if(!proyectoDTO.isValid()){
            throw new ValidationException("Datos ingresados inválidos");
        }
        Proyecto newProyecto = new Proyecto(proyectoDTO);

        Long caseId = this.bonitaService.iniciarProcesoCreacionProyecto(newProyecto.getNombre());

        // TODO: ENVIAR A CLOUD LOS PEDIDOS

        newProyecto.setCaseId(caseId);

        this.proyectoRepository.save(newProyecto);

    }

    @Override
    public List<ProyectoDTO> getProyectos() {
        List<Long> userCaseIds = this.bonitaService.getUserProcessesCaseIds(NombresProcesos.PROCESO_CREAR_PROYECTO);

        return ProyectoDTO.fromEntity(this.proyectoRepository.findByCaseIdIn(userCaseIds));
    }

    @Override
    public Role roleForService() {
        return Role.ONG_SOL;
    }

    @Override
    public ProyectoDTO getProyecto(Long proyectoId) throws ValidationException {
        ProyectoDTO proyectoDTO =  ProyectoDTO.fromEntity(
                this.proyectoRepository.findById(proyectoId)
                        .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"))
        );

        List<Long> userCaseIds = this.bonitaService.getUserProcessesCaseIds(NombresProcesos.PROCESO_CREAR_PROYECTO);

        if (!userCaseIds.contains(proyectoDTO.caseId())) {
            throw new ValidationException("No tienes permiso para visualizar este proyecto.");
        }

        // bonitaService -> Obtener compromisos de colaboración

        return proyectoDTO;
    }
}
