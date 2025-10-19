package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDTO {

    private String username;
    private String nombreOng;
//    private List<PedidoColaboracionDTO> pedidosColaboracion;
//    private List<CompromisoColaboracionDTO> compromisoColaboracion;


    public static UserDTO fromEntity(User entity) {
        return UserDTO.builder()
                .username(entity.getUsername())
                .nombreOng(entity.getNombreOng())
//                .pedidosColaboracion()
//                .compromisoColaboracion()
                .build();
    }
}
