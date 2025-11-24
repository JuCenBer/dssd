package grupo16.dssd_backend.dtos.cloud;

import com.fasterxml.jackson.annotation.JsonInclude;
import grupo16.dssd_backend.models.Recurso;
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
    private CloudUserDTO userPedido;
    private ProyectoDTO proyectoPedido;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Recurso recurso;
    private Boolean completado;
    private List<CompromisoColaboracionDTO> compromisosColaboracion;


}
