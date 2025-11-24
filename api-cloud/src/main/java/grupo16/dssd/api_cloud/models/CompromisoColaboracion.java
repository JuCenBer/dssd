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

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private PedidoColaboracion pedidoColaboracion;

    @Builder.Default
    private Boolean cumplido = false;

}
