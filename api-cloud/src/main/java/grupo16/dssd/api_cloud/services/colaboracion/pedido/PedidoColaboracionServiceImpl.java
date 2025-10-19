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
    public PedidoColaboracion crearPedidoColaboracion(PedidoColaboracionDTO pedidoDTO, User userPedido, Proyecto proyecto){

        PedidoColaboracion pedidoColaboracion = PedidoColaboracion.builder()
                .userPedido(userPedido)
                .proyectoPedido(proyecto)
                .nombre(pedidoDTO.getNombre())
                .fechaInicio(pedidoDTO.getFechaInicio())
                .fechaFin(pedidoDTO.getFechaFin())
                .recurso(pedidoDTO.getRecurso())
                .completado(Boolean.FALSE)
                .compromisosColaboracion(new ArrayList<CompromisoColaboracion>())
                .build();
        proyecto.getPedidosColaboracion().add(pedidoColaboracion);
        userPedido

        return this.pedidoColaboracionRepository.save(pedidoColaboracion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoColaboracionDTO> getPedidoColaboracionByUsuarioOrganizador(User userOrganizador) {
        return List.of();
    }


}
