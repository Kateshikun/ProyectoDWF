package sv.edu.udb.config;

// Importamos las clases para trabajar con filtros HTTP (Servlet)
import jakarta.servlet.FilterChain;           // Cadena de filtros (uno tras otro)
import jakarta.servlet.ServletException;      // Excepciones de Servlet
import jakarta.servlet.http.HttpServletRequest;   // Petición HTTP entrante
import jakarta.servlet.http.HttpServletResponse;  // Respuesta HTTP saliente
import lombok.RequiredArgsConstructor;


import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;  // Almacena la autenticación actual
import org.springframework.security.core.userdetails.UserDetails;         // Datos del usuario
import org.springframework.security.core.userdetails.UserDetailsService;  // Carga usuarios
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;  // Filtro que se ejecuta una vez por petición

import sv.edu.udb.service.JwtService;
import java.io.IOException;

//Clase que funciona para autenticar cada usuario.
//Filtra peticiones HTTP entrantes
//Hereda de OncePerRequestFilter porque solo se aplicara una vez por petición
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;           // Servicio para validar tokens JWT
    private final UserDetailsService userDetailsService;  // Servicio para cargar datos de usuarios

    /**
     * Este método se ejecuta automáticamente por cada petición HTTP que llega.
     * @param request     La petición HTTP entrante (contiene headers, URL, etc.)
     * @param response    La respuesta HTTP que enviaremos de vuelta
     * @param filterChain La cadena de filtros (para pasar al siguiente filtro)
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Verificar si la URL es pública (Como el login)
        final String requestPath = request.getRequestURI();  // Obtiene la URL completa
        
        // Si la petición va a rutas de autenticación, la dejamos pasar sin revisar token
        if (requestPath.startsWith("/api/auth/")) {      // Login, registro
            
            filterChain.doFilter(request, response); //Pasa al siguiente filtro
            return;
        }

        //Si no, buscamos el header de autorización
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Si no hay header de autorización, o no empieza con "Bearer ",
        // significa que no viene token. Para endpoints protegidos, esto debería ser 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No bloqueamos aquí, dejamos que Spring Security maneje la autorización
            // basada en la configuración de requestMatchers
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el token JWT del header (quitamos "Bearer " que son 7 caracteres)
        jwt = authHeader.substring(7);

        //Validamos token. Si esta malo, saltara excepcion.
        try {
            // Extraemos el username del token
            username = jwtService.extractUsername(jwt);

            //Si no fuimos autenticados, procedemos
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Cargamos los datos del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                //Verificar el token sea valido
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // Creamos el objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,           // El usuario autenticado
                                    null,                  // Credentials (no necesarios con JWT)
                                    userDetails.getAuthorities()  // Sus roles/permisos
                            );
                    
                    // Agregamos detalles de la petición (IP, navegador, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Guardamos la autenticación en el contexto de seguridad
                    // SecurityContextHolder es como la "sesión" de seguridad de Spring.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                // Si el token no es válido, no hacemos nada.
                // La petición continúa sin autenticación.
            }
        } catch (Exception e) {
            // Si hay cualquier error (token inválido, expirado, firma incorrecta...)
            // Logueamos el error pero dejamos que la petición continúe.
            logger.error("Error validando token JWT: " + e.getMessage());
        }

        // Siempre debemos llamar a filterChain.doFilter() para que la petición
        // llegue a su destino (el controller) o al siguiente filtro.
        filterChain.doFilter(request, response);
    }
}
