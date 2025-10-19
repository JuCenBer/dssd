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

    @OneToMany(mappedBy = "pedidoColaboracion", orphanRemoval = true)
    private List<CompromisoColaboracion> compromisosColaboracion;


    public PedidoColaboracion(User userPedido, Proyecto proyectoPedido, Boolean completado, String nombre, LocalDate fechaInicio, LocalDate fechaFin, Recurso recurso) {

        this.userPedido = userPedido;
        this.proyectoPedido = proyectoPedido;
        this.completado = completado;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.recurso = recurso;
        this.setCompromisosColaboracion(new ArrayList<CompromisoColaboracion>());

        userPedido.getPedidosColaboracion().add(this);
        proyectoPedido.getPedidosColaboracion().add(this);

    }
}
