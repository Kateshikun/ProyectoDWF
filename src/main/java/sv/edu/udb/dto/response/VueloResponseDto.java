package sv.edu.udb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de vuelo
 * Contiene los datos planos necesarios para mostrar un vuelo sin referencias circulares
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VueloResponseDto {
    private Long id_vuelo;
    
    // Datos de la ruta
    private Long id_ruta;
    private String origen;
    private String ciudad_origen;
    private String pais_origen;
    private String destino;
    private String ciudad_destino;
    private String pais_destino;
    private Double distancia_km;
    
    // Datos del avión
    private Long id_avion;
    private Integer capacidad_pasajeros;
    
    // Datos del vuelo
    private LocalDateTime fecha_salida;
    private LocalDateTime fecha_llegada;
    private String estado;
    private String nombre_piloto;
    private String nombre_copiloto;
}
