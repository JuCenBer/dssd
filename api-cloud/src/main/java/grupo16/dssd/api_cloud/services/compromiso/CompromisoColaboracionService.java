package grupo16.dssd.api_cloud.services.compromiso;

import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.CompromisoColaboracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompromisoColaboracionService implements I_CompromisoColaboracionService {

    private final CompromisoColaboracionRepository compromisoRepository;


    @Override
    public CompromisoColaboracionDTO crearCompromisoColaboracion(CompromisoColaboracionDTO compromisoDTO, User user, PedidoColaboracion pedido) {
        CompromisoColaboracion compromiso = new CompromisoColaboracion(null, user, compromisoDTO.getDescripcion(), pedido, false);
        compromiso = this.compromisoRepository.save(compromiso);
        return CompromisoColaboracionDTO.fromEntity(compromiso, true);
    }
}
