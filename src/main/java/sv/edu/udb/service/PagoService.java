package sv.edu.udb.service;

import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.dto.response.PagoResponse;
import sv.edu.udb.model.Pago;

import java.util.List;

public interface PagoService {
    Pago procesarPago(PagoDTO pagoDTO);
    PagoResponse consultarPagoPorReserva(Long idReservacion);
    List<PagoResponse> listarTodos();
}
