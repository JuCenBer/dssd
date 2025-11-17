package grupo16.dssd.api_cloud.repositories;

import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompromisoColaboracionRepository extends JpaRepository<CompromisoColaboracion, Long> {
    
}
