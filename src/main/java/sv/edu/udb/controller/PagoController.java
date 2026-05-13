package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.model.Pago;
import sv.edu.udb.service.PagoService;
import sv.edu.udb.service.ReservacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Endpoints para gestión de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ReservacionService reservacionService;

    @Operation(summary = "Procesar un pago")
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        try {
            // Procesar el pago
            Pago pagoProcesado = pagoService.procesarPago(pagoDTO);

            // Si el pago es exitoso, confirmar la reserva asociada
            reservacionService.confirmarReserva(pagoDTO.getIdReservacion());

            return ResponseEntity.ok("Pago procesado exitosamente. ID de pago: " + pagoProcesado.getIdPago());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Consultar pago por reservación")
    @GetMapping("/reserva/{idReservacion}")
    public ResponseEntity<Pago> consultarPagoPorReserva(@PathVariable Long idReservacion) {
        try {
            Pago pago = pagoService.consultarPagoPorReserva(idReservacion);
            if (pago != null) {
                return ResponseEntity.ok(pago);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
