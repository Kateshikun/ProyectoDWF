package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.request.ReservacionDTO;
import sv.edu.udb.dto.response.ReservacionResponseDto;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.service.ReservacionService;
import sv.edu.udb.util.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservaciones", description = "Endpoints para gestión de reservaciones")
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    @Operation(summary = "Crear una nueva reservación")
    @PostMapping("/")
    public ResponseEntity<ReservacionResponseDto> crearReservacion(@Valid @RequestBody ReservacionDTO reservacionDTO) {
        Reservacion reservacionCreada = reservacionService.crearReservacion(reservacionDTO);
        ReservacionResponseDto response = convertirAReservacionResponseDto(reservacionCreada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener detalle de una reservación específica")
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionResponseDto> obtenerReservacion(@PathVariable Long id) {
        Reservacion reservacion = reservacionService.obtenerPorId(id);
        
        // Verificar que el usuario sea ADMIN o el dueño de la reservación
        if (!UserUtils.isAdmin()) {
            // Si no es ADMIN, verificar que la reservación pertenezca al usuario actual
            // Aquí necesitaríamos una forma de obtener el ID del usuario desde el token
            // y compararlo con el pasajero de la reservación
            // Por ahora, asumimos que el usuario puede acceder a sus propias reservaciones
        }
        
        ReservacionResponseDto response = convertirAReservacionResponseDto(reservacion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas las reservas de un pasajero específico")
    @GetMapping("/pasajero/{id}")
    public ResponseEntity<List<ReservacionResponseDto>> listarReservasPorPasajero(@PathVariable Long id) {
        // Verificar que el usuario sea ADMIN o esté solicitando sus propias reservas
        if (!UserUtils.isAdmin()) {
            // Aquí necesitaríamos verificar que el ID del pasajero corresponda al usuario actual
            // Por ahora, permitimos el acceso pero en producción se debería verificar
        }
        
        List<Reservacion> reservas = reservacionService.listarPorPasajero(id);
        List<ReservacionResponseDto> response = reservas.stream()
                .map(this::convertirAReservacionResponseDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancelar una reserva existente")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReservacion(@PathVariable Long id) {
        Reservacion reservacion = reservacionService.obtenerPorId(id);
        
        // Verificar que el usuario sea ADMIN o el dueño de la reservación
        if (!UserUtils.isAdmin()) {
            // Si no es ADMIN, verificar que la reservación pertenezca al usuario actual
            // Aquí necesitaríamos una forma de obtener el ID del usuario desde el token
            // y compararlo con el pasajero de la reservación
            // Por ahora, asumimos que el usuario puede cancelar sus propias reservaciones
        }
        
        reservacionService.cancelarReservacion(id);
        return ResponseEntity.ok("Reserva cancelada exitosamente");
    }

    @Operation(summary = "Confirmar una reserva")
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmarReserva(@PathVariable Long id) {
        reservacionService.confirmarReserva(id);
        return ResponseEntity.ok("Reserva confirmada exitosamente");
    }

    private ReservacionResponseDto convertirAReservacionResponseDto(Reservacion reservacion) {
        return ReservacionResponseDto.builder()
                .id_reservacion(reservacion.getId_reservacion())
                .id_vuelo(reservacion.getVuelo() != null ? reservacion.getVuelo().getId_vuelo() : null)
                .codigo_vuelo(reservacion.getVuelo() != null ? "VUE-" + reservacion.getVuelo().getId_vuelo() : null)
                .origen(reservacion.getVuelo() != null && reservacion.getVuelo().getRuta() != null && 
                        reservacion.getVuelo().getRuta().getOrigen() != null ? 
                        reservacion.getVuelo().getRuta().getOrigen().getNombre() : null)
                .destino(reservacion.getVuelo() != null && reservacion.getVuelo().getRuta() != null && 
                        reservacion.getVuelo().getRuta().getDestino() != null ? 
                        reservacion.getVuelo().getRuta().getDestino().getNombre() : null)
                .fecha_salida(reservacion.getVuelo() != null ? reservacion.getVuelo().getFecha_salida() : null)
                .fecha_llegada(reservacion.getVuelo() != null ? reservacion.getVuelo().getFecha_llegada() : null)
                .id_pasajero(reservacion.getPasajero() != null ? reservacion.getPasajero().getId_pasajero() : null)
                .nombre_pasajero(reservacion.getPasajero() != null ? reservacion.getPasajero().getNombre() : null)
                .apellido_pasajero(reservacion.getPasajero() != null ? reservacion.getPasajero().getApellido() : null)
                .pasaporte_pasajero(reservacion.getPasajero() != null ? reservacion.getPasajero().getPasaporte() : null)
                .fecha_reservacion(reservacion.getFecha_reservacion())
                .estado_reserva(reservacion.getEstado_reserva())
                .asiento_asignado(reservacion.getAsiento_asignado())
                .build();
    }
}
