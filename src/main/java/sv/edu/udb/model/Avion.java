package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "AVION")
@AllArgsConstructor
@NoArgsConstructor
public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_avion;

    @NotNull(message = "La capacidad de pasajeros no puede estar vacía")
    @Min(value = 1, message = "La capacidad debe ser al menos de 1 pasajero")
    @Max(value = 850, message = "La capacidad no puede exceder los 850 pasajeros")
    @Column(nullable = false)
    private Integer capacidad_pasajeros;


    @NotNull(message = "Debe asignar una aerolínea al avión")
    @ManyToOne(fetch = FetchType.LAZY) //Solo se cargara cuando se use
    @JoinColumn(name = "id_aerolinea", nullable = false)
    private Aerolinea aerolinea;
}
