package grupo16.dssd.api_cloud.repositories;

import grupo16.dssd.api_cloud.models.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacionRepository extends JpaRepository<Observacion, Long> {
}
