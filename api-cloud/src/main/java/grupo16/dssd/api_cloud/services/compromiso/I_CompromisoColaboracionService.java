package grupo16.dssd.api_cloud.services.compromiso;

import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.User;

public interface I_CompromisoColaboracionService {

    CompromisoColaboracionDTO crearCompromisoColaboracion(CompromisoColaboracionDTO compromisoDTO, User user, PedidoColaboracion pedido);
}
