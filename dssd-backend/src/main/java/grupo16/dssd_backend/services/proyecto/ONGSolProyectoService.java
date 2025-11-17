package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.helpers.NombresProcesos;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
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
    public List<Integer> getProyectos() {
        return this.bonitaService.getUserProcessesCaseIds(NombresProcesos.PROCESO_CREAR_PROYECTO);
    }

    @Override
    public Role roleForService() {
        return Role.ONG_SOL;
    }
}
