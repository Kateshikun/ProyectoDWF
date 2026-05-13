package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "AEROLINEA")
@AllArgsConstructor
@NoArgsConstructor
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aerolinea")
    private Long idAerolinea;

    @NotBlank(message = "El nombre de la aerolínea es requerido") // Validación campo vacío
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El código ICAO es obligatorio")
    @Pattern(regexp = "^[A-Z]{3}$", message = "El código ICAO debe consistir en 3 letras mayúsculas") // Validación tipo de dato (Regex)
    @Column(nullable = false, unique = true, length = 10)
    private String codigo_icao;

    @OneToMany(mappedBy = "aerolinea", cascade = CascadeType.ALL)
    private List<Avion> aviones;

}

