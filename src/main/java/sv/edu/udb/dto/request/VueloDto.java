package sv.edu.udb.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Dto para el motor de busqueda
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VueloDto {
    private Long id_vuelo;

    @NotNull(message = "La ruta no puede ser nula")
    private Long id_ruta;
    @NotNull(message = "Debe especificar el ID del avión")
    private Long id_avion;
    @NotNull(message = "La fecha de salida es obligatoria")
    @Future(message = "La fecha de salida debe ser una fecha futura")
    private LocalDateTime fecha_salida;
    @NotNull(message = "La fecha de llegada es obligatoria")
    private LocalDateTime fecha_llegada;
    @NotBlank(message = "El estado del vuelo es obligatorio (Ej: Programado)")
    private String estado;
    @NotBlank(message = "El nombre del piloto es obligatorio")
    @Size(min = 3, max = 100)
    private String nombre_piloto;
    @NotBlank(message = "El nombre del copiloto es obligatorio")
    @Size(min = 3, max = 100)
    private String nombre_copiloto;
}
