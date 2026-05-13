package sv.edu.udb.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Pago
 * Utilizado en operaciones de procesamiento de pagos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    
    @NotNull(message = "El ID de la reservación es obligatorio")
    private Long idReservacion;

    @Positive(message = "El monto del pago debe ser un valor positivo")
    private Double monto;

    @Pattern(regexp = "^\\d{16}$", message = "El número de tarjeta debe tener exactamente 16 dígitos")
    private String numeroTarjeta;

    @Pattern(regexp = "^\\d{3}$", message = "El CVV debe tener exactamente 3 dígitos")
    private String cvv;
}
