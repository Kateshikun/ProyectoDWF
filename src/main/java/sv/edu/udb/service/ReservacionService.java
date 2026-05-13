package sv.edu.udb.service;

import sv.edu.udb.dto.request.ReservacionDTO;
import sv.edu.udb.model.Reservacion;

import java.util.List;

public interface ReservacionService {
    Reservacion crearReservacion(ReservacionDTO reservacionDTO);
    Reservacion obtenerPorId(Long id);
    List<Reservacion> listarPorPasajero(Long idPasajero);
    void cancelarReservacion(Long id);
    void confirmarReserva(Long idReservacion);
}
