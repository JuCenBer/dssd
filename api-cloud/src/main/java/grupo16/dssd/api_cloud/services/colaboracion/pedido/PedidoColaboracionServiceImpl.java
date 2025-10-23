package grupo16.dssd.api_cloud.services.colaboracion.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.PedidoColaboracionRepository;
import grupo16.dssd.api_cloud.repositories.UserRepository;
import grupo16.dssd.api_cloud.services.proyecto.I_ProyectoService;
import grupo16.dssd.api_cloud.services.proyecto.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoColaboracionServiceImpl implements I_PedidoColaboracionService {

    private final PedidoColaboracionRepository pedidoColaboracionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PedidoColaboracionDTO crearPedidoColaboracion(PedidoColaboracionDTO pedidoDTO, User userPedido, Proyecto proyecto){

        PedidoColaboracion pedidoColaboracion = new PedidoColaboracion(
                pedidoDTO.getNombre(),
                pedidoDTO.getFechaInicio(),
                pedidoDTO.getFechaFin(),
                pedidoDTO.getRecurso(),
                Boolean.FALSE,
                userPedido,
                proyecto);

        pedidoColaboracion = this.pedidoColaboracionRepository.save(pedidoColaboracion);

        return PedidoColaboracionDTO.fromEntity(pedidoColaboracion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoColaboracionDTO> getPedidoColaboracionByUsuarioOrganizador(User userOrganizador) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoColaboracionDTO> findAll() {
        return PedidoColaboracionDTO.fromEntity(this.pedidoColaboracionRepository.findAll());
    }

    @Override
    public List<PedidoColaboracionDTO> findByOng(String ong) {
        return PedidoColaboracionDTO.fromEntity(this.pedidoColaboracionRepository.findByUserPedido_NombreOng(ong));
    }

    public Page<PedidoColaboracionDTO> findByProject(int page, int size, long Id){
        Pageable pageable = PageRequest.of(page, size);
        Page<PedidoColaboracion> pedidoColaboracionPage = pedidoColaboracionRepository.findByProyectoPedido_Id(Id, pageable);

        return pedidoColaboracionPage.map(PedidoColaboracionDTO::fromEntity);
    }


}
