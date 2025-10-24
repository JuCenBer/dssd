package grupo16.dssd.api_cloud.services.compromiso;

import grupo16.dssd.api_cloud.dtos.CompromisoColaboracionDTO;
import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.CompromisoColaboracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompromisoColaboracionService implements I_CompromisoColaboracionService {

    private final CompromisoColaboracionRepository compromisoRepository;


    @Override
    public CompromisoColaboracionDTO crearCompromisoColaboracion(CompromisoColaboracionDTO compromisoDTO, User user, PedidoColaboracion pedido) {

        CompromisoColaboracion compromiso = CompromisoColaboracion.builder()
                .userCompromiso(user)
                .descripcion((compromisoDTO.getDescripcion()))
                .pedidoColaboracion(pedido)
                .cumplido(Boolean.FALSE)
                .build();

        pedido.getCompromisosColaboracion().add(compromiso);

        compromiso = this.compromisoRepository.save(compromiso);
        return CompromisoColaboracionDTO.fromEntity(compromiso, Boolean.FALSE);
    }

    @Override
    public Optional<CompromisoColaboracion> findById(Long id) {
        return this.compromisoRepository.findById(id);
    }
}
