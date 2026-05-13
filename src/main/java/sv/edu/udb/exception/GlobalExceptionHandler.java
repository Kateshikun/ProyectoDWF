package sv.edu.udb.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import sv.edu.udb.dto.response.ErrorResponseDTO;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Centraliza el manejo de errores y proporciona respuestas consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de campos (@Valid).
     * Ocurre cuando los datos enviados en una petición no cumplen las reglas de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        System.out.println("🔍 ERROR DE VALIDACIÓN: " + ex.getMessage());
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        // Extraer errores de validación por campo
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            System.out.println("❌ Campo '" + error.getField() + "': " + error.getDefaultMessage());
            validationErrors.put(error.getField(), error.getDefaultMessage());
        });
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de Validación")
                .message("Los datos enviados contienen errores. Por favor, verifica los campos marcados.")
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Maneja excepciones de violación de restricciones (validaciones a nivel de método/clase).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {
        
        System.out.println("🔍 ERROR DE VALIDACIÓN DE RESTRICCIONES: " + ex.getMessage());
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            System.out.println("❌ Restricción '" + field + "': " + message);
            validationErrors.put(field, message);
        });
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de Validación")
                .message("Los datos enviados violan algunas restricciones. Por favor, verifica los campos.")
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Maneja excepciones de recurso no encontrado.
     * Ocurre cuando se intenta acceder a un recurso que no existe.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request) {
        
        System.out.println("🔍 RECURSO NO ENCONTRADO: " + ex.getMessage());
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso No Encontrado")
                .message("El recurso solicitado no existe o no está disponible en este momento.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    
    /**
     * Maneja excepciones de acceso denegado.
     * Ocurre cuando un usuario intenta acceder a un recurso para el cual no tiene permisos.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request) {
        
        System.out.println("🔍 ACCESO DENEGADO: " + ex.getMessage());
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acceso Denegado")
                .message("No tienes permisos para acceder a este recurso. Por favor, contacta al administrador si crees que esto es un error.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Maneja excepciones de argumentos ilegales.
     * Ocurre cuando se pasan argumentos inválidos a un método.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        System.out.println("🔍 ARGUMENTO INVÁLIDO: " + ex.getMessage());
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Argumento Inválido")
                .message("Los datos proporcionados son inválidos o están en formato incorrecto.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Maneja todas las excepciones no específicas.
     * Este es el manejador por defecto para cualquier excepción no capturada por los demás.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        System.out.println("🔍 ERROR INTERNO DEL SERVIDOR: " + ex.getMessage());
        ex.printStackTrace(); // Imprimir stack trace para depuración
        System.out.println("📍 PATH: " + request.getDescription(false));
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error Interno del Servidor")
                .message("Ha ocurrido un error inesperado. Por favor, intenta nuevamente más tarde o contacta al soporte técnico.")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
