package grupo16.dssd.api_cloud.services.proyecto;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.dtos.bonita.CreacionProyectoDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.ProyectoRepository;
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

}
