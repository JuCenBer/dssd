package grupo16.dssd_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import grupo16.dssd_backend.dtos.ActividadDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private Recurso recurso;

    private Boolean requiereColaboracion;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    public Actividad(){

    }

    public Actividad(ActividadDTO actividadDTO, Proyecto proyecto) {
        this.id = actividadDTO.id();
        this.nombre = actividadDTO.nombre();
        this.fechaInicio = actividadDTO.fechaInicio();
        this.fechaFin = actividadDTO.fechaFin();
        this.recurso = Recurso.valueOf(actividadDTO.recurso());
        this.requiereColaboracion = actividadDTO.requiereColaboracion();
        this.proyecto = proyecto;
    }


}
