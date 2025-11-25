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
    private PedidoColaboracionDTO pedidoColaboracion;

    public static CompromisoColaboracionDTO fromEntity(CompromisoColaboracion compromiso, Boolean withPedido) {
        CompromisoColaboracionDTOBuilder builder = CompromisoColaboracionDTO.builder();

        if(withPedido) builder.pedidoColaboracion(PedidoColaboracionDTO.fromEntity(compromiso.getPedidoColaboracion(), false));

        return builder
                .id(compromiso.getId())
                .userCompromiso(UserDTO.fromEntity(compromiso.getUserCompromiso()))
                .descripcion(compromiso.getDescripcion())
                .build();
    }

    public static List<CompromisoColaboracionDTO> fromEntity(List<CompromisoColaboracion> compromisos, Boolean withPedido) {
        return compromisos.stream().map((compromiso) -> CompromisoColaboracionDTO.fromEntity(compromiso, withPedido))
                .toList();
    }
}
