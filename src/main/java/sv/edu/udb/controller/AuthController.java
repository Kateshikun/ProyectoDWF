package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Importamos clases de Spring para respuestas HTTP y seguridad
import org.springframework.http.ResponseEntity;  // Para construir respuestas HTTP (200, 401, etc.)
import org.springframework.security.authentication.AuthenticationManager;      // Gestor de autenticación
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // Token de login
import org.springframework.security.core.Authentication;  // Representa una autenticación exitosa
import org.springframework.security.core.userdetails.UserDetails;  // Datos del usuario
import org.springframework.web.bind.annotation.*;  // Anotaciones para crear endpoints REST

// Importamos nuestras clases del proyecto
import sv.edu.udb.config.UserDetailsImpl;  // Adaptador que envuelve nuestra entidad Usuario
import sv.edu.udb.dto.request.AuthRequestDto;
import sv.edu.udb.dto.response.AuthResponseDTO;
import sv.edu.udb.dto.request.UsuarioDTO;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.service.JwtService;
import sv.edu.udb.service.UsuarioService;

// Importamos anotaciones de Swagger/OpenAPI para documentación
import io.swagger.v3.oas.annotations.Operation;  // Describe qué hace cada endpoint
import io.swagger.v3.oas.annotations.tags.Tag;    // Agrupa endpoints bajo una etiqueta

/*
 * Esta clase el "portero" de nuestra aplicación. Maneja TODO lo relacionado con el acceso
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios")
@RequiredArgsConstructor
public class AuthController {

    // Servicio para registrar y buscar usuarios en la base de datos
    private final UsuarioService usuarioService;
    
    // Servicio para crear y validar tokens JWT
    private final JwtService jwtService;
    
    // Gestor que valida credenciales (username/password) contra la base de datos
    private final AuthenticationManager authenticationManager;


    /*
     * Endpoint para iniciar sesión con username y password.
     * Flujo del login:
     * 1. El cliente envía username y password en el body (JSON)
     * 2. @Valid verifica que los datos cumplan las reglas (no vacíos, etc.)
     * 3. authenticationManager valida las credenciales contra la base de datos
     * 4. Si son válidas, generamos un token JWT
     * 5. Devolvemos el token al cliente para que lo use en futuras peticiones
     */
    @Operation(summary = "Iniciar sesión con usuario y contraseña")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDto authRequest) {
        try {
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),      // Username del request
                            authRequest.getPassword()       // Password en texto plano
                    )
            );
            // Si llegamos aquí, la autenticación fue exitosa
            // Si falla, lanza una excepción y vamos al catch

            // Extraemos los datos del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generamos el token JWT
            String jwtToken = jwtService.generateToken(userDetails);

            // Extraemos el rol del usuario
            String rol = userDetails.getAuthorities().stream()
                    .findFirst()  // Tomamos el primer rol
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .orElse("CLIENTE");  // Si no tiene rol, asumimos CLIENTE


            // Creamos el DTO de respuesta con los datos que necesita el cliente
            AuthResponseDTO response = AuthResponseDTO.builder()
                    .token(jwtToken)              // El token JWT para futuras peticiones
                    .username(userDetails.getUsername())  // Nombre del usuario
                    .rol(rol)                     // Rol del usuario (ADMIN/CLIENTE)
                    .build();

            // Devolvemos HTTP 200 (OK) con el objeto JSON
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Si algo falla (usuario no existe, contraseña incorrecta, etc.)
            // Devolvemos HTTP 401 (Unauthorized) sin body
            return ResponseEntity.status(401).build();
        }
    }

    /*
     * Endpoint para registrar un nuevo usuario en el sistema.
     * Flujo del registro:
     * 1. El cliente envía los datos del nuevo usuario (username, password, email, rol)
     * 2. @Valid verifica que los datos cumplan las reglas de UsuarioDTO
     * 3. UsuarioService guarda el usuario en la base de datos (con password encriptado)
     * 4. Generamos un token JWT para que el usuario pueda usar la API inmediatamente
     * 5. Devolvemos el token (el usuario queda logueado automáticamente)
     */
    @Operation(summary = "Registrar nuevo usuario y obtener token JWT")
    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registro(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Registramos el usuario en la base de datos
            
            Usuario usuarioRegistrado = usuarioService.registrar(usuarioDTO);

            // Creamos UserDetailsImpl a partir del usuario registrado
            // Necesitamos envolver el Usuario en UserDetailsImpl para que
            // nuestro JwtService pueda generar el token (espera un UserDetails)
            UserDetailsImpl userDetails = new UserDetailsImpl(usuarioRegistrado);
            
            // Generamos un token JWT para el nuevo usuario
            String jwtToken = jwtService.generateToken(userDetails);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .token(jwtToken)
                    .username(usuarioRegistrado.getUsername())
                    .rol(usuarioRegistrado.getRol())
                    .build();

            // Devolvemos HTTP 200 (OK) con el token
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            // Si hay algún error (email duplicado, username duplicado, etc.)
            // Devolvemos HTTP 400 (Bad Request)
            return ResponseEntity.badRequest().build();
        }
    }
}
