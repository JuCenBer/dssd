package grupo16.dssd.api_cloud.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import grupo16.dssd.api_cloud.models.PedidoColaboracion;
import grupo16.dssd.api_cloud.models.Recurso;
import grupo16.dssd.api_cloud.models.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoColaboracionDTO {

    private Long id;
    private UserDTO userPedido;
    private ProyectoDTO proyectoPedido;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Recurso recurso;
    private Boolean completado;
//    private List<CompromisoColaboracionDTO> compromisosColaboracion;

    public static PedidoColaboracionDTO fromEntity(PedidoColaboracion pedido, Boolean withProyecto) {
        PedidoColaboracionDTOBuilder builder = PedidoColaboracionDTO.builder();

        if (withProyecto) builder.proyectoPedido(ProyectoDTO.fromEntity(pedido.getProyectoPedido()));

        return builder
                .id(pedido.getId())
                .nombre(pedido.getNombre())
                .fechaInicio(pedido.getFechaInicio())
                .fechaFin(pedido.getFechaFin())
                .completado(pedido.getCompletado())
                .recurso(pedido.getRecurso())
                .userPedido(UserDTO.fromEntity(pedido.getUserPedido()))
//                .compromisosColaboracion(CompromisoColaboracionDTO.fromEntity(pedido.getCompromisosColaboracion()))
                .build();
    }

    public static List<PedidoColaboracionDTO> fromEntity(List<PedidoColaboracion> pedidos, Boolean withProyecto) {
        return pedidos.stream()
                .map((pedido) -> PedidoColaboracionDTO.fromEntity(pedido, withProyecto))
                .toList();
    }

}
