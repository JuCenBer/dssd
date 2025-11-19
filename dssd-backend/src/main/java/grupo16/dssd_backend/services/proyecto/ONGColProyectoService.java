package grupo16.dssd_backend.services.proyecto;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.models.Actividad;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.models.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ONGColProyectoService extends AbstractProyectoService {

    // Va a poder ver los proyectos que tienen pedidos de colaboración
    @Override
    public List<ProyectoDTO> getProyectos() {
        List<Actividad> actividadesColaboracion = this.actividadRepository.findByRequiereColaboracion(Boolean.TRUE);

        Set<Proyecto> proyectosSet = new HashSet<>();

        for (Actividad act : actividadesColaboracion) {
            proyectosSet.add(act.getProyecto());
        }

        return ProyectoDTO.fromEntity(proyectosSet);
    }

    @Override
    public Role roleForService() {
        return Role.ONG_COL;
    }

    @Override
    public ProyectoDTO getProyecto(Long proyectoId) {
        ProyectoDTO proyectoDTO =  ProyectoDTO.fromEntity(
                this.proyectoRepository.findById(proyectoId)
                        .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"))
        );

        // TODO: bonitaService -> Obtener compromisos de colaboración
        // TODO: Para el ONG_COL, va a poder ver el proyecto si en esos compromisos está él
        
        return proyectoDTO;
    }
}
