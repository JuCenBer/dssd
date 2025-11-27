package grupo16.dssd_backend.models;

import grupo16.dssd_backend.dtos.ProyectoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private Long caseId;

    private String descripcion;

    private String ubicacion;

    @Transient
    private EstadoProyecto estado;

    private Long externalId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyecto", orphanRemoval = true)
    private List<Actividad> actividades;

    public Proyecto(){
        this.actividades = new ArrayList<Actividad>();
    }

    public Proyecto(ProyectoDTO proyectoDTO){
        this.id = proyectoDTO.id();
        this.nombre = proyectoDTO.nombre();
        this.caseId = proyectoDTO.caseId();
        this.descripcion = proyectoDTO.descripcion();
        this.ubicacion = proyectoDTO.ubicacion();
        this.actividades = new ArrayList<>(
                proyectoDTO.actividades().stream()
                        .map(actDTO -> new Actividad(actDTO, this))
                        .toList()
        );
        this.estado = EstadoProyecto.EN_PLANIFICACION;
        this.externalId = null;
    }
}
