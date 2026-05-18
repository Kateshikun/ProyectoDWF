package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.edu.udb.model.enums.EstadoVuelo;

import java.time.LocalDateTime;

@Entity
@Table(name = "VUELO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_vuelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_avion")
    private Avion avion;

    @NotNull
    @Future(message = "La fecha de salida debe ser futura")
    private LocalDateTime fecha_salida;

    @NotNull
    private LocalDateTime fecha_llegada;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoVuelo estado;

    @NotBlank(message = "Debe asignar un piloto")
    private String nombre_piloto;

    @NotBlank(message = "Debe asignar un copiloto")
    private String nombre_copiloto;
}
