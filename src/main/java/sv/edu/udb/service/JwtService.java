package sv.edu.udb.service;

import io.jsonwebtoken.Claims;           // Contiene los datos (claims) del token
import io.jsonwebtoken.Jwts;              // Clase principal para crear y leer tokens
import io.jsonwebtoken.SignatureAlgorithm; // Algoritmo de firma (HS256)
import io.jsonwebtoken.io.Decoders;        // Para decodificar en Base64
import io.jsonwebtoken.security.Keys;      // Para generar claves seguras

import org.springframework.beans.factory.annotation.Value;  // Para leer valores de application.properties
import org.springframework.security.core.userdetails.UserDetails; // Representa un usuario en Spring Security
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;  // Tipo de clave para firmar tokens
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Estructura de un JWT:
 * - HEADER: Indica el tipo (JWT) y algoritmo de firma (HS256)
 * - PAYLOAD: Datos del usuario (username, rol, expiración)
 * - SIGNATURE: Firma digital que verifica que el token no fue alterado
 */
@Service
public class JwtService {

    // Estos valores se leen del archivo application.properties
    // Si no existen, usan los valores por defecto que están después de los :
    @Value("${jwt.secret}")
    private String secretKey;  // Clave secreta para firmar los tokens

    @Value("${jwt.expiration}")  // 24 horas
    private long jwtExpiration;

    // Extrae el username del token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Extrae cualquier dato específico del token
     * claimsResolver Función que indica qué dato extraer
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /*
     * Metodo 1 para crear tokens: Token sin datos extras
     * Le pasamos los datos del usuario y nos
     * devuelve un token listo para enviar al cliente.
     */
    public String generateToken(UserDetails userDetails) {
        // Llamamos al método de JWT con un mapa vacío (sin datos extra)
        return generateToken(new HashMap<>(), userDetails);
    }

    /*
     * Metodo 2: un token JWT con datos adicionales personalizados
     * 
     * extraClaims: Datos adicionales que queremos incluir en el token
     * userDetails: Datos del usuario autenticado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // El rol viene en formato "ROLE_ADMIN" o "ROLE_CLIENTE", así que quitamos el prefijo
        String rol = userDetails.getAuthorities().stream()
                .findFirst()  // Tomamos el primer rol (asumimos un rol por usuario)
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("CLIENTE");  // Si no tiene rol, asumimos CLIENTE por defecto
        
        extraClaims.put("rol", rol);
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // --------------- Metodos de apoyo ---------------

     // Este método es el "ensamblador" que pega todas las partes del token
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Firmar con clave secreta
                .compact();
    }


    /*
     * Verifica si un token es válido y auténtico
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Extraemos el username del token
        final String username = extractUsername(token);
        
        // Verificamos si es token valido: Username coincide y no esta vencido
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /*
     * Verifica si el token ha expirado (está vencido)
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /*
     * Extrae la fecha de expiración del token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /*
     * Abre el token JWT y extrae TODOS sus claims (datos)
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()           // Creamos un "parser" (lector de tokens)
                .setSigningKey(getSignInKey())  // Le damos la clave secreta para verificar firma
                .build()                       // Construimos el parser
                .parseClaimsJws(token)          // Parseamos el token (verifica firma automáticamente)
                .getBody();                     // Extraemos el payload (los claims)
    }

    /*
     * Obtiene la clave secreta para firmar/verificar tokens
     */
    private SecretKey getSignInKey() {
        // Convertimos la clave de Base64 a bytes
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Creamos una clave HMAC-SHA256 con esos bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * Extrae el rol del usuario desde el token
     */
    public String extractRol(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }
}
