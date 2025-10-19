package grupo16.dssd.api_cloud.services.proyecto;

import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Proyecto crearProyecto(ProyectoDTO proyectoDTO, User cargadoPor) {
        Proyecto proyecto = Proyecto.builder()
                .caseId(proyectoDTO.getCaseId())
                .name(proyectoDTO.getName())
                .description(proyectoDTO.getDescription())
                .ubicacion(proyectoDTO.getUbicacion())
                .pedidosColaboracion(new ArrayList<PedidoColaboracion>())
                .cargadoPor(cargadoPor)
                .build();
        return this.proyectoRepository.save(proyecto);
    }


}
