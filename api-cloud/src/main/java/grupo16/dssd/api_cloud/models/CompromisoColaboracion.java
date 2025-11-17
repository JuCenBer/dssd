package grupo16.dssd.api_cloud.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class CompromisoColaboracion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_compromiso_id", nullable = false)
    private User userCompromiso;

    private String descripcion;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoColaboracion pedidoColaboracion;

    @Builder.Default
    private Boolean cumplido = false;

}
