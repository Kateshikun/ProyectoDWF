package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.dto.response.PagoResponse;
import sv.edu.udb.service.PagoService;
import sv.edu.udb.service.ReservacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Endpoints para gestion de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ReservacionService reservacionService;

    @Operation(summary = "Procesar un pago")
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        pagoService.procesarPago(pagoDTO);
        reservacionService.confirmarReserva(pagoDTO.getIdReservacion());
        return ResponseEntity.ok("Pago procesado exitosamente");
    }

    @Operation(summary = "Consultar pago por reservacion")
    @GetMapping("/reserva/{idReservacion}")
    public ResponseEntity<PagoResponse> consultarPagoPorReserva(@PathVariable Long idReservacion) {
        PagoResponse pago = pagoService.consultarPagoPorReserva(idReservacion);
        if (pago != null) {
            return ResponseEntity.ok(pago);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar todos los pagos")
    @GetMapping("/")
    public ResponseEntity<List<PagoResponse>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }
}
