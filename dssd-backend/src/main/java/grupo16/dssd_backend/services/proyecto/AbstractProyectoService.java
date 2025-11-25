package grupo16.dssd_backend.services.proyecto;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupo16.dssd_backend.dtos.ActividadDTO;
import grupo16.dssd_backend.dtos.ProyectoDTO;
import grupo16.dssd_backend.dtos.cloud.ColaboracionDTO;
import grupo16.dssd_backend.exceptions.RoleException;
import grupo16.dssd_backend.exceptions.ValidationException;
import grupo16.dssd_backend.models.Actividad;
import grupo16.dssd_backend.models.Proyecto;
import grupo16.dssd_backend.repositories.ActividadRepository;
import grupo16.dssd_backend.repositories.ProyectoRepository;
import grupo16.dssd_backend.services.bonita.I_BonitaService;
import grupo16.dssd_backend.services.cloud.I_CloudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProyectoService implements I_ProyectoService {

    @Autowired
    protected ProyectoRepository proyectoRepository;

    @Autowired
    protected ActividadRepository actividadRepository;

    @Autowired
    protected I_BonitaService bonitaService;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected I_CloudService cloudService;

    @Override
    public ProyectoDTO createProject(ProyectoDTO proyectoDTO) throws RoleException, ValidationException {
        throw new RoleException("No tiene el rol necesario para realizar esta acción");
    }

    @Override
    public ProyectoDTO getProyecto(Long proyectoId) throws ValidationException {
        Proyecto proyecto = this.proyectoRepository.findById(proyectoId)
                        .orElseThrow(()-> new EntityNotFoundException("Proyecto no encontrado"));

        ProyectoDTO proyectoDTO = ProyectoDTO.fromEntity(proyecto);

        List<ActividadDTO> actividadesColaborativas = this.cloudService.getProyectoDetails(proyecto).actividades()
                .stream().map(actividadDTO ->
                        new ActividadDTO(
                                actividadDTO.id(),
                                actividadDTO.nombre(),
                                actividadDTO.fechaInicio(),
                                actividadDTO.fechaFin(),
                                actividadDTO.recurso(),
                                Boolean.TRUE, //Se realiza el map para poder corregir este dato, dado que se instancia como NULL.
                                actividadDTO.colaboracion())).toList();


        List<ActividadDTO> actividadesNoColaborativas =
                proyectoDTO.actividades().stream().filter(actividadDTO -> !actividadDTO.requiereColaboracion()).toList();


        List<ActividadDTO> actividades = new ArrayList<ActividadDTO>();
        actividades.addAll(actividadesNoColaborativas);
        actividades.addAll(actividadesColaborativas);


        proyectoDTO = new ProyectoDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getUbicacion(),
                proyecto.getCaseId(),
                actividades,
                proyecto.getEstado()
        );
        return proyectoDTO;
    }
}
