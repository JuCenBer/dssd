package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.Recurso;
import grupo16.dssd.api_cloud.models.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PedidoColaboracionDTO {

    private Long id;
    private UserDTO userPedido;
    private ProyectoDTO proyectoPedido;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Recurso recurso;
    private Boolean completado;
    private List<CompromisoColaboracionDTO> compromisosColaboracion;
}
