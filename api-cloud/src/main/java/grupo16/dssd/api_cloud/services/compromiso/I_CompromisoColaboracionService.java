package grupo16.dssd.api_cloud.services.compromiso;

import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.User;

import java.util.Optional;

public interface I_CompromisoColaboracionService {

    CompromisoColaboracionDTO crearCompromisoColaboracion(CompromisoColaboracionDTO compromisoDTO, User user, PedidoColaboracion pedido);

    Optional<CompromisoColaboracion> findById(Long id);

    @Deprecated
    CompromisoColaboracionDTO cumplirCompromiso(CompromisoColaboracion compromiso);

}
