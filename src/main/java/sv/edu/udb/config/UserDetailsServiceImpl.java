package sv.edu.udb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.UsuarioService;

/*
 * UserDetailsServiceImpl - El "Buscador de Usuarios" para Spring Security
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UsuarioService usuarioService;

    /*
     * Busca un usuario en la base de datos por su username.
     * Este método es llamado AUTOMÁTICAMENTE por Spring Security cada vez que:
     * - Alguien intenta hacer login
     * - El JwtAuthenticationFilter necesita validar un token JWT
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // buscarPorUsername devuelve Optional<Usuario>, que puede contener el usuario
        // o estar vacío si no se encontró.
        
        Usuario usuario = usuarioService.buscarPorUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));
        
        // Envolvemos el Usuario en UserDetailsImpl
        
        return new UserDetailsImpl(usuario);
        
        // A partir de aquí, Spring Security tiene acceso a:
        // - username
        // - password (encriptada)
        // - roles
        // - estado de la cuenta (activo/inactivo)
    }
}
