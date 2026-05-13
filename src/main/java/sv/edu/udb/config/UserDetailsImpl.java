package sv.edu.udb.config;

import lombok.AllArgsConstructor;
import lombok.Data;

// Importamos las interfaces y clases de Spring Security
import org.springframework.security.core.GrantedAuthority;       // Representa un permiso/rol
import org.springframework.security.core.authority.SimpleGrantedAuthority;  // Implementación simple de permiso
import org.springframework.security.core.userdetails.UserDetails; // Interfaz que Spring Security usa para usuarios

import sv.edu.udb.model.Usuario;

import java.util.Collection;
import java.util.Collections;

/*
*Esta clase es como el adaptador entre nuestra entidad y Spring Security
 */
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;

    //Spring Security llama a estos metodos cuando los necesita
    //Este metodo devuelve los permisos (roles) del usuario ADMIN/CLIENTE
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rol = usuario.getRol() != null ? usuario.getRol() : "CLIENTE";
        
        // Creamos una lista con UNA autoridad: el rol del usuario
        // SimpleGrantedAuthority es la implementación más simple de GrantedAuthority
        // Spring Security espera el prefijo "ROLE_" para los roles
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    /*
     * Devuelve la CONTRASEÑA del usuario la cual debe ser encriptada
     */
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    /*
     * Devuelve el USERNAME (nombre de usuario) para identificarlo.
     */
    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    // MÉTODOS DE ESTADO DE LA CUENTA
    // Estos métodos le dicen a Spring Security si la cuenta puede usarse.


    @Override
    public boolean isAccountNonExpired() {
        return true;  // Por ahora, todas las cuentas son permanentes
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Por ahora, no implementamos bloqueo de cuentas
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Por ahora, las contraseñas no expiran
    }

    /*
     * Este metodo VERIFICA el campo 'estado' de nuestra entidad Usuario.
     */
    @Override
    public boolean isEnabled() {
        return "ACTIVO".equalsIgnoreCase(usuario.getEstado());
    }

}
