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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservaciones", description = "Endpoints para gestion de reservaciones")
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    @Operation(summary = "Crear una nueva reservacion")
    @PostMapping("/")
    public ResponseEntity<ReservacionResponseDto> crearReservacion(@Valid @RequestBody ReservacionDTO reservacionDTO) {
        Reservacion creada = reservacionService.crearReservacion(reservacionDTO);
        return ResponseEntity.ok(mapToResponse(creada));
    }

    @Operation(summary = "Obtener detalle de una reservacion")
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionResponseDto> obtenerReservacion(@PathVariable Long id) {
        Reservacion reservacion = reservacionService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(reservacion));
    }

    @Operation(summary = "Listar todas las reservaciones")
    @GetMapping("/")
    public ResponseEntity<List<ReservacionResponseDto>> listarTodas() {
        List<ReservacionResponseDto> response = reservacionService.listarTodas().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar reservas de un pasajero")
    @GetMapping("/pasajero/{id}")
    public ResponseEntity<List<ReservacionResponseDto>> listarReservasPorPasajero(@PathVariable Long id) {
        List<ReservacionResponseDto> response = reservacionService.listarPorPasajero(id).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancelar una reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReservacion(@PathVariable Long id) {
        reservacionService.cancelarReservacion(id);
        return ResponseEntity.ok("Reserva cancelada exitosamente");
    }

    @Operation(summary = "Confirmar una reserva")
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmarReserva(@PathVariable Long id) {
        reservacionService.confirmarReserva(id);
        return ResponseEntity.ok("Reserva confirmada exitosamente");
    }

    private ReservacionResponseDto mapToResponse(Reservacion r) {
        return ReservacionResponseDto.builder()
                .id_reservacion(r.getId_reservacion())
                .id_vuelo(r.getVuelo() != null ? r.getVuelo().getId_vuelo() : null)
                .codigo_vuelo(r.getVuelo() != null ? "VUE-" + r.getVuelo().getId_vuelo() : null)
                .origen(r.getVuelo() != null && r.getVuelo().getRuta() != null &&
                        r.getVuelo().getRuta().getOrigen() != null ?
                        r.getVuelo().getRuta().getOrigen().getNombre() : null)
                .destino(r.getVuelo() != null && r.getVuelo().getRuta() != null &&
                        r.getVuelo().getRuta().getDestino() != null ?
                        r.getVuelo().getRuta().getDestino().getNombre() : null)
                .fecha_salida(r.getVuelo() != null ? r.getVuelo().getFecha_salida() : null)
                .fecha_llegada(r.getVuelo() != null ? r.getVuelo().getFecha_llegada() : null)
                .id_pasajero(r.getPasajero() != null ? r.getPasajero().getId_pasajero() : null)
                .nombre_pasajero(r.getPasajero() != null ? r.getPasajero().getNombre() : null)
                .apellido_pasajero(r.getPasajero() != null ? r.getPasajero().getApellido() : null)
                .pasaporte_pasajero(r.getPasajero() != null ? r.getPasajero().getPasaporte() : null)
                .fecha_reservacion(r.getFecha_reservacion())
                .estado_reserva(r.getEstado_reserva() != null ? r.getEstado_reserva().getLabel() : null)
                .asiento_asignado(r.getAsiento_asignado())
                .build();
    }
}
