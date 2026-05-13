package sv.edu.udb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservacionResponseDto {
    
    private Long id_reservacion;
    
    private Long id_vuelo;
    private String codigo_vuelo;
    private String origen;
    private String destino;
    private LocalDateTime fecha_salida;
    private LocalDateTime fecha_llegada;
    
    private Long id_pasajero;
    private String nombre_pasajero;
    private String apellido_pasajero;
    private String pasaporte_pasajero;
    
    private LocalDateTime fecha_reservacion;
    private String estado_reserva;
    private String asiento_asignado;
}
