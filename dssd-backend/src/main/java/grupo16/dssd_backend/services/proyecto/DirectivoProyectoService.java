package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectivoProyectoService extends AbstractProyectoService {


    @Override
    public List<ProyectoDTO> getProyectos() {
        return ProyectoDTO.fromEntity(this.proyectoRepository.findAll());
    }

    @Override
    public Role roleForService() {
        return Role.DIRECTIVO;
    }

    @Override
    public ProyectoDTO getProyecto(Long proyectoId) {

        ProyectoDTO proyectoDTO =  ProyectoDTO.fromEntity(
                this.proyectoRepository.findById(proyectoId)
                        .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"))
        );

        // TODO: bonitaService -> Obtener compromisos de colaboración

        return proyectoDTO;
    }
}
