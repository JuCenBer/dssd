package grupo16.dssd.api_cloud.services.proyecto;

import grupo16.dssd.api_cloud.dtos.ObservacionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoCompletoDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.dtos.bonita.CreacionProyectoDTO;
import grupo16.dssd.api_cloud.models.*;
import grupo16.dssd.api_cloud.repositories.ObservacionRepository;
import grupo16.dssd.api_cloud.repositories.ProyectoRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoService implements I_ProyectoService {

    private final ProyectoRepository proyectoRepository;

    private final ObservacionRepository observacionRepository;

    @Override
    public Optional<Proyecto> findById(Long id) {
        return this.proyectoRepository.findById(id);
    }

    @Override
    @Transactional
    public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO, User cargadoPor) {

        Proyecto proyecto = Proyecto.builder()
                .caseId(proyectoDTO.getCaseId())
                .nombre(proyectoDTO.getNombre())
                .descripcion(proyectoDTO.getDescripcion())
                .ubicacion(proyectoDTO.getUbicacion())
                .pedidosColaboracion(new ArrayList<PedidoColaboracion>())
                .cargadoPor(cargadoPor)
                .estado(proyectoDTO.getEstado())
                .observaciones(new ArrayList<Observacion>())
                .build();

        proyecto = this.proyectoRepository.save(proyecto);

        return ProyectoDTO.fromEntity(proyecto);
    }

    @Override
    public ProyectoDTO crearProyecto(CreacionProyectoDTO creacionProyectoDTO, User cargadoPor) {

        ProyectoDTO proyectoDTO = ProyectoDTO.builder()
                .caseId(creacionProyectoDTO.caseId())
                .nombre(creacionProyectoDTO.nombre())
                .descripcion(creacionProyectoDTO.descripcion())
                .ubicacion(creacionProyectoDTO.ubicacion())
                .estado(EstadoProyecto.EN_PLANIFICACION)
                .build();

        return this.crearProyecto(proyectoDTO, cargadoPor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> findAll() {
        return ProyectoDTO.fromEntity(this.proyectoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proyecto> findByCaseId(Long caseId) {
        return this.proyectoRepository.findByCaseId(caseId);
    }

    @Override
    @Transactional
    public Proyecto updateEstado(Proyecto proyecto, EstadoProyecto estado) {

        if (estado.equals(EstadoProyecto.FINALIZADO)) {
            if (proyecto.getObservaciones().stream().anyMatch(obs -> !obs.getResuelto())) {
                throw new ValidationException("Proyecto no puede ser finalizado. Tiene observaciones por resolver.");
            }
        }

        proyecto.setEstado(estado);

        return this.proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public ObservacionDTO agregarObservacion(Proyecto proyecto, ObservacionDTO observacionDTO, User hechoPor) {

        Observacion observacion = Observacion.builder()
                .proyecto(proyecto)
                .comentario(observacionDTO.getComentario())
                .resuelto(Boolean.FALSE)
                .hechoPor(hechoPor)
                .caseId(observacionDTO.getCaseId())
                .build();

        proyecto.getObservaciones().add(observacion);

        observacion = this.observacionRepository.save(observacion);

        return ObservacionDTO.fromEntity(observacion);
    }

    @Override
    @Transactional
    public ProyectoCompletoDTO resolverObservacion(Proyecto proyecto, Long idObservacion) {

        Observacion observacion = this.observacionRepository.findById(idObservacion)
                .orElseThrow();

        if (!proyecto.getObservaciones().contains(observacion)) {
            throw new ValidationException("La observación no pertenece al proyecto indicado.");
        }

        observacion.setResuelto(Boolean.TRUE);

        this.observacionRepository.save(observacion);

        return ProyectoCompletoDTO.fromEntity(observacion.getProyecto());
    }

}
