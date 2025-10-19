package grupo16.dssd.api_cloud.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private Long caseId;

    private String descripcion;

    private String ubicacion;

    @ManyToOne
    @JoinColumn(name = "cargado_por_id", nullable = false)
    private User cargadoPor;

    @OneToMany(mappedBy = "proyectoPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoColaboracion> pedidosColaboracion;
}
