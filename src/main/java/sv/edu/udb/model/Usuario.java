package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8) // Para seguridad JWT
    private String password;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "El rol es obligatorio (ADMIN/CLIENTE)")
    private String rol; //ADMIN/CLIENTE

    @NotBlank(message = "Especifique el estado del usuario (ACTIVO/INACTIVO)")
    private String estado; //ACTIVO/INACTIVO
}