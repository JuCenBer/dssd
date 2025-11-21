package grupo16.dssd.api_cloud.services.pedido;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.dtos.ProyectoDTO;
import grupo16.dssd.api_cloud.dtos.bonita.CreacionActividadDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;

import java.util.List;
import java.util.Optional;

public interface I_PedidoColaboracionService {

    PedidoColaboracionDTO crearPedidoColaboracion(PedidoColaboracionDTO pedido, User userPedido, Proyecto proyecto);

    void crearPedidosColaboracion(List<CreacionActividadDTO> actividades, Proyecto proyecto);

    List<PedidoColaboracionDTO> getPedidoColaboracionByUsuarioOrganizador(User userOrghanizador);

    List<PedidoColaboracionDTO> findAll();

    Optional<PedidoColaboracion> findById(Long id);


}
