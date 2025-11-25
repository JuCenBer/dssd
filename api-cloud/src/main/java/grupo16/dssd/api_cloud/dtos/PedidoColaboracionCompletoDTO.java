package grupo16.dssd.api_cloud.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Recurso;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PedidoColaboracionCompletoDTO {

    private Long id;
    private UserDTO userPedido;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Recurso recurso;
    private CompromisoColaboracionDTO colaboracion;

    public static PedidoColaboracionCompletoDTO fromEntity(PedidoColaboracion pedido) {
        return PedidoColaboracionCompletoDTO.builder()
                .id(pedido.getId())
                .nombre(pedido.getNombre())
                .fechaInicio(pedido.getFechaInicio())
                .fechaFin(pedido.getFechaFin())
                .recurso(pedido.getRecurso())
                .userPedido(UserDTO.fromEntity(pedido.getUserPedido()))
                .colaboracion(pedido.getColaboracion() != null ?
                        CompromisoColaboracionDTO.fromEntity(pedido.getColaboracion(), Boolean.FALSE)
                        : null)
                .build();
    }

    public static List<PedidoColaboracionCompletoDTO> fromEntity(List<PedidoColaboracion> pedidos) {
        return pedidos.stream()
                .map(PedidoColaboracionCompletoDTO::fromEntity)
                .toList();
    }
}
