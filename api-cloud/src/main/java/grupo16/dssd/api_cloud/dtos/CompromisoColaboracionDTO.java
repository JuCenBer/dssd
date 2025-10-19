package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.CompromisoColaboracion;
import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
public class CompromisoColaboracionDTO {

    private Long id;
    private UserDTO userCompromiso;
    private String descripcion;
//    private PedidoColaboracionDTO pedidoColaboracion;
    private Boolean cumplido;

    public static CompromisoColaboracionDTO fromEntity(CompromisoColaboracion compromiso) {
        return CompromisoColaboracionDTO.builder()
                .id(compromiso.getId())
                .userCompromiso(UserDTO.fromEntity(compromiso.getUserCompromiso()))
                .descripcion(compromiso.getDescripcion())
                .cumplido(compromiso.getCumplido())
                .build();
    }

    public static List<CompromisoColaboracionDTO> fromEntity(List<CompromisoColaboracion> compromisos) {
        return compromisos.stream().map(CompromisoColaboracionDTO::fromEntity)
                .toList();
    }
}
