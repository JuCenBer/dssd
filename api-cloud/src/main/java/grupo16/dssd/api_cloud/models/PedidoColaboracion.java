package grupo16.dssd.api_cloud.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class PedidoColaboracion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_pedido_id", nullable = false)
    private User userPedido;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="proyecto_pedido_id", nullable = false)
    private Proyecto proyectoPedido;

    private Boolean completado;

    // Atributos de la Actividad:
    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private Recurso recurso;

    @OneToOne
    private CompromisoColaboracion colaboracion;

    public PedidoColaboracion(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Recurso recurso, Boolean completado, User userPedido, Proyecto proyectoPedido) {

        this.userPedido = userPedido;
        this.proyectoPedido = proyectoPedido;
        this.completado = completado;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.recurso = recurso;
        this.colaboracion = null;

        userPedido.getPedidosColaboracion().add(this);
        proyectoPedido.getPedidosColaboracion().add(this);

    }
}
