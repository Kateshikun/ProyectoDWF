package sv.edu.udb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 //DTO para transferencia de datos de Pasajero
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroDTO {
    
    @NotBlank(message = "El nombre del pasajero es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido del pasajero es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El número de pasaporte es obligatorio")
    @Size(min = 6, max = 20, message = "El pasaporte debe tener entre 6 y 20 caracteres")
    private String pasaporte;

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Size(min = 3, max = 50, message = "La nacionalidad debe tener entre 3 y 50 caracteres")
    private String nacionalidad;
}
