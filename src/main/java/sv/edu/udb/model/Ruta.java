package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "RUTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ruta;

    @ManyToOne
    @JoinColumn(name = "id_aeropuerto_origen")
    private Aeropuerto origen;

    @ManyToOne
    @JoinColumn(name = "id_aeropuerto_destino")
    private Aeropuerto destino;

    @Positive(message = "La distancia debe ser mayor a 0")
    private Double distancia_km;

    private LocalTime duracion_estimada;
}
