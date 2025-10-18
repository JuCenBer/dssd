package grupo16.dssd.api_cloud.services.requests;

import grupo16.dssd.api_cloud.dtos.PedidoColaboracionDTO;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Proyecto;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.CollaborationRequestRepository;
import grupo16.dssd.api_cloud.repositories.ProjectRepository;
import grupo16.dssd.api_cloud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationRequestService {

    private final CollaborationRequestRepository collaborationRequestRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public PedidoColaboracion createCollaborationRequest(PedidoColaboracionDTO collaborationRequest, User user){
        Proyecto proyecto = this.projectRepository.getReferenceById(collaborationRequest.getProyectoId());
        PedidoColaboracion pedidoColaboracion = PedidoColaboracion.builder()
                .userPedido(user)
                .proyectoPedido(proyecto)
                .nombre(collaborationRequest.getNombre())
                .fechaInicio(collaborationRequest.getFechaInicio())
                .fechaFin(collaborationRequest.getFechaFin())
                .recurso(collaborationRequest.getRecurso())
                .build();
        proyecto.getPedidosColaboracion().add(pedidoColaboracion);
        return this.collaborationRequestRepository.save(pedidoColaboracion);
    }

    @Transactional
    public List<PedidoColaboracionDTO> getCollaborationRequestByOrganizer(String usernameOrganizador){
        List<PedidoColaboracionDTO> pedidoColaboracionDTO = null;

        return pedidoColaboracionDTO;
    }
}
