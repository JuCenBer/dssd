package grupo16.dssd_backend.repositories;

import grupo16.dssd_backend.models.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByRequiereColaboracion(Boolean requiereColaboracion);
}
