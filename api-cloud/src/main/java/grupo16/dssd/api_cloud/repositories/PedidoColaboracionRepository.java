package grupo16.dssd.api_cloud.repositories;

import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoColaboracionRepository extends JpaRepository<PedidoColaboracion, Long> {

    List<PedidoColaboracion> findByUserPedido_NombreOng(String ong);
}
