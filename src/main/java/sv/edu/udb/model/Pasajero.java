package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PASAJERO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pasajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pasajero;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    private String apellido;

    @NotBlank(message = "El pasaporte es obligatorio")
    @Pattern(regexp = "^[A-Z0-9]{6,15}$", message = "Formato de pasaporte inválido") // Regex
    @Column(unique = true)
    private String pasaporte;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
