package grupo16.dssd.api_cloud.repositories;

import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoColaboracionRepository extends JpaRepository<PedidoColaboracion, Long> {

    List<PedidoColaboracion> findByUserPedido_NombreOng(String ong);

    Page<PedidoColaboracion> findByProyectoPedido_Id(Long Id, Pageable pageable);

    List<PedidoColaboracion> findByProyectoPedido(Proyecto proyecto);
}
