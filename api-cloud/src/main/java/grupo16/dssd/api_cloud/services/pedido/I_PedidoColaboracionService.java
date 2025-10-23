package grupo16.dssd.api_cloud.services.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface I_PedidoColaboracionService {

    PedidoColaboracionDTO crearPedidoColaboracion(PedidoColaboracionDTO pedido, User userPedido, Proyecto proyecto);

    List<PedidoColaboracionDTO> getPedidoColaboracionByUsuarioOrganizador(User userOrghanizador);

    List<PedidoColaboracionDTO> findAll();

//    List<PedidoColaboracionDTO> findByOng(String ong);
//
//    Page<PedidoColaboracionDTO> findByProyecto(int page, int size, long id);
//
//    List<PedidoColaboracionDTO> findByProyecto(Proyecto proyecto);

}
