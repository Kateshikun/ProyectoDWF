package sv.edu.udb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

 //DTO para transferencia de datos de Reservación

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservacionDTO {
    
    @NotNull(message = "El ID del vuelo es obligatorio")
    private Long idVuelo;

    @NotNull(message = "El ID del pasajero es obligatorio")
    private Long idPasajero;

    @Pattern(regexp = "^[0-9]{1,2}[A-F]$", message = "El formato del asiento debe ser como '12A' o '4B' (número seguido de letra A-F)")
    private String asientoAsignado;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    private String estadoReserva = "PENDIENTE";
}
