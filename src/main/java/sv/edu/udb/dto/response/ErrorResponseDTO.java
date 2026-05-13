package sv.edu.udb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

//Dto para cuando algo salga mal mandar mensajes con estructura amigable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String error;
    private String path;

    //Objeto con la lista de errores de validacion
    private Map<String, String> validationErrors;
}
