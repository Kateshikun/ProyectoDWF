package sv.edu.udb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Importamos clases de autenticación de Spring Security
import org.springframework.security.authentication.AuthenticationManager;      // Gestiona el proceso de login
import org.springframework.security.authentication.AuthenticationProvider;   // Provee autenticación
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;  // Autenticación con base de datos
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// Importamos clases de configuración web de Spring Security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;  // Configura seguridad HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;  // Habilita seguridad web
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;  // Configuradores HTTP
import org.springframework.security.config.http.SessionCreationPolicy;  // Política de sesiones

// Importamos clases de manejo de usuarios y contraseñas
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  // Encriptador de contraseñas
import org.springframework.security.crypto.password.PasswordEncoder;    // Interfaz de encriptación

// Importamos clases del filtro de seguridad
import org.springframework.security.web.SecurityFilterChain;  // Cadena de filtros de seguridad
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;  // Filtro de login
import org.springframework.web.cors.CorsConfiguration;  // Configuración CORS
import org.springframework.web.cors.CorsConfigurationSource;  // Fuente de configuración CORS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;  // Fuente basada en URLs para CORS

/*
 * Esta clase es el CENTRO DE CONTROL de toda la seguridad de nuestra aplicación.
 * Aquí definimos QUIÉN puede acceder a QUÉ recursos y CÓMO se autentican los usuarios.
 */
@Configuration
@EnableWebSecurity  // Habilita y configura Spring Security
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    // Nuestro filtro JWT que valida tokens en cada petición
    private final JwtAuthenticationFilter jwtAuthFilter;
    
    // Servicio que carga usuarios desde la base de datos
    private final UserDetailsService userDetailsService;

    /*
     * Este es el método MÁS IMPORTANTE. Define TODAS las reglas de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configuramos CORS
            .csrf(AbstractHttpConfigurer::disable)  // Desactivamos CSRF

            // Aquí definimos qué URLs están permitidas y cuáles requieren autenticación.
            
            .authorizeHttpRequests(auth -> auth

                // AuthController: Login y registro son públicos (prioridad alta)
                .requestMatchers("/api/auth/**").permitAll()
                
                // Swagger/OpenAPI: Documentación pública para desarrolladores
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                                
                // AdminController: SOLO usuarios con rol ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // VuelosController: Permitir acceso público para listar vuelos
                .requestMatchers("/api/vuelos/**").permitAll()
                
                // CUALQUIER OTRA RUTA: Requiere estar autenticado (ADMIN o CLIENTE)
                .anyRequest().hasAnyRole("ADMIN", "CLIENTE")
            )

            
            // Cada petición es independiente y lleva su propio token JWT.
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Le decimos a Spring Security cómo autenticar usuarios (base de datos + BCrypt)
            .authenticationProvider(authenticationProvider())

            // Insertamos nuestro filtro JWT ANTES del filtro de autenticación estándar.
            // Así validamos el token antes de cualquier otra cosa.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Construimos y devolvemos la cadena de filtros configurada
        return http.build();
    }

    /*
     * Este componente se encarga de:
     * 1. Buscar al usuario en la base de datos (UserDetailsService)
     * 2. Verificar que la contraseña sea correcta (PasswordEncoder con BCrypt)
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // Configuramos dónde buscar los usuarios
        authProvider.setUserDetailsService(userDetailsService);
        
        // Configuramos cómo verificar las contraseñas (BCrypt)
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    /*
     * Se crea el "AuthenticationManager" que coordina el proceso de login.
     * Es como el supervisor que coordina entre el filtro de seguridad y el proveedor
     * de autenticación para completar el proceso de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
     * Crea el encriptador de contraseñas usando BCrypt.
     * Es el estándar de la industria para guardar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Configura CORS para permitir peticiones desde diferentes orígenes
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir orígenes específicos (ajusta según necesites)
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        
        // Permitir métodos HTTP específicos
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Permitir headers específicos
        configuration.setAllowedHeaders(java.util.List.of("*"));
        
        // Permitir credenciales (cookies, headers de autorización)
        configuration.setAllowCredentials(true);
        
        // Configurar tiempo máximo de cache para respuestas preflight
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
