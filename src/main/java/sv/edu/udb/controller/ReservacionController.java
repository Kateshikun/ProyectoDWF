package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.request.ReservacionDTO;
import sv.edu.udb.dto.response.ReservacionResponseDto;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.service.ReservacionService;
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
        try {
            Reservacion reservacionCreada = reservacionService.crearReservacion(reservacionDTO);
            ReservacionResponseDto response = convertirAReservacionResponseDto(reservacionCreada);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener detalle de una reservación específica")
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionResponseDto> obtenerReservacion(@PathVariable Long id) {
        try {
            Reservacion reservacion = reservacionService.obtenerPorId(id);
            ReservacionResponseDto response = convertirAReservacionResponseDto(reservacion);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todas las reservas de un pasajero específico")
    @GetMapping("/pasajero/{id}")
    public ResponseEntity<List<ReservacionResponseDto>> listarReservasPorPasajero(@PathVariable Long id) {
        List<Reservacion> reservas = reservacionService.listarPorPasajero(id);
        List<ReservacionResponseDto> response = reservas.stream()
                .map(this::convertirAReservacionResponseDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancelar una reserva existente")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReservacion(@PathVariable Long id) {
        try {
            reservacionService.cancelarReservacion(id);
            return ResponseEntity.ok("Reserva cancelada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Confirmar una reserva")
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmarReserva(@PathVariable Long id) {
        try {
            reservacionService.confirmarReserva(id);
            return ResponseEntity.ok("Reserva confirmada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
