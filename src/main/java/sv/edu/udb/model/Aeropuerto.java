package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aeropuerto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aeropuerto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_aeropuerto;

    @NotBlank(message = "Nombre del aeropuerto requerido")
    private String nombre;

    @NotBlank(message = "Ciudad requerida")
    private String ciudad;

    @NotBlank(message = "País requerido")
    private String pais;

    @NotBlank(message = "Código IATA requerido")
    @Size(min = 3, max = 3)
    @Column(unique = true)
    private String codigo_iata;
}
