package grupo16.dssd.api_cloud.dtos;

import grupo16.dssd.api_cloud.models.Observacion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ObservacionDTO {

    private Long id;
    private String comentario;
    private Boolean resuelto;
    private UserDTO hechoPor;
    private Long caseId;

    static ObservacionDTO fromEntity(Observacion observacion) {
        return ObservacionDTO.builder()
                .id(observacion.getId())
                .comentario(observacion.getComentario())
                .resuelto(observacion.getResuelto())
                .hechoPor(UserDTO.fromEntity(observacion.getHechoPor()))
                .caseId(observacion.getCaseId())
                .build();
    }

    static List<ObservacionDTO> fromEntity(List<Observacion> observaciones) {
        return observaciones.stream()
                .map(ObservacionDTO::fromEntity)
                .toList();
    }
}
