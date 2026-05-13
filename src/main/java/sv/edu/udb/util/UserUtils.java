package sv.edu.udb.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utilidad para obtener información del usuario autenticado
 */
public class UserUtils {

    /**
     * Obtiene el username del usuario autenticado actualmente
     * @return username del usuario o null si no está autenticado
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    /**
     * Verifica si el usuario autenticado tiene el rol especificado
     * @param role rol a verificar (sin prefijo "ROLE_")
     * @return true si tiene el rol, false en caso contrario
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
        }
        return false;
    }

    /**
     * Verifica si el usuario autenticado es ADMIN
     * @return true si es ADMIN, false en caso contrario
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Verifica si el usuario autenticado es CLIENTE
     * @return true si es CLIENTE, false en caso contrario
     */
    public static boolean isCliente() {
        return hasRole("CLIENTE");
    }
}
