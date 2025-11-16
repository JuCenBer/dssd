package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ONGSolProyectoService extends AbstractProyectoService {

    @Override
    public void createProject(ProyectoDTO proyectoDTO) throws ValidationException {

        if(!proyectoDTO.validate()){
            throw new ValidationException("Datos ingresados inválidos");
        }
        Proyecto newProyecto = new Proyecto(proyectoDTO);

        Long caseId = this.bonitaService.iniciarProcesoCreacionProyecto(newProyecto.getNombre());

        newProyecto.setCaseId(caseId);

        this.proyectoRepository.save(newProyecto);

    }

    @Override
    public List<ProyectoDTO> getProyectos() {
        return List.of();
    }

    @Override
    public Role roleForService() {
        return Role.ONG_SOL;
    }
}
