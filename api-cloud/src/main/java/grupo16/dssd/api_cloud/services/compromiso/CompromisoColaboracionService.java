package grupo16.dssd.api_cloud.services.compromiso;

import grupo16.dssd.api_cloud.repositories.CompromisoColaboracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompromisoColaboracionService implements I_CompromisoColaboracionService {

    private final CompromisoColaboracionRepository compromisoRepository;



}
