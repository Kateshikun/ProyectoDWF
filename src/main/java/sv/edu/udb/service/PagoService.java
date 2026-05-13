package sv.edu.udb.service;

import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.model.Pago;

public interface PagoService {
    Pago procesarPago(PagoDTO pagoDTO);
    Pago consultarPagoPorReserva(Long idReservacion);
}
