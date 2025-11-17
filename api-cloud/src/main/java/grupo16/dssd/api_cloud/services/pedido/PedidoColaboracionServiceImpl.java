package grupo16.dssd.api_cloud.services.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.PedidoColaboracionRepository;
import grupo16.dssd.api_cloud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        return PedidoColaboracionDTO.fromEntity(pedidoColaboracion, Boolean.TRUE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoColaboracionDTO> getPedidoColaboracionByUsuarioOrganizador(User userOrganizador) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoColaboracionDTO> findAll() {
        return PedidoColaboracionDTO.fromEntity(this.pedidoColaboracionRepository.findAll(), Boolean.TRUE);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoColaboracion> findById(Long id) {
        return this.pedidoColaboracionRepository.findById(id);
    }


}
