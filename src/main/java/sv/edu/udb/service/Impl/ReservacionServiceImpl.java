package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.ReservacionDTO;
import sv.edu.udb.model.Pasajero;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.model.Vuelo;
import sv.edu.udb.model.enums.EstadoReserva;
import sv.edu.udb.repository.ReservacionRepository;
import sv.edu.udb.service.ReservacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservacionServiceImpl implements ReservacionService {

    private final ReservacionRepository reservacionRepository;

    @Override
    public Reservacion crearReservacion(ReservacionDTO reservacionDTO) {
        if (reservacionDTO.getIdVuelo() == null) {
            throw new IllegalArgumentException("La reservacion debe estar asociada a un vuelo");
        }
        if (reservacionDTO.getIdPasajero() == null) {
            throw new IllegalArgumentException("La reservacion debe estar asociada a un pasajero");
        }

        Reservacion reservacion = new Reservacion();
        Vuelo vuelo = new Vuelo();
        vuelo.setId_vuelo(reservacionDTO.getIdVuelo());
        reservacion.setVuelo(vuelo);

        Pasajero pasajero = new Pasajero();
        pasajero.setId_pasajero(reservacionDTO.getIdPasajero());
        reservacion.setPasajero(pasajero);

        reservacion.setAsiento_asignado(reservacionDTO.getAsientoAsignado());
        reservacion.setEstado_reserva(EstadoReserva.fromString(reservacionDTO.getEstadoReserva()));

        if (reservacion.getEstado_reserva() == null) {
            reservacion.setEstado_reserva(EstadoReserva.PENDIENTE);
        }

        if (reservacionDTO.getAsientoAsignado() != null && !reservacionDTO.getAsientoAsignado().trim().isEmpty()) {
            List<Reservacion> existentes = reservacionRepository.buscarAsientoOcupado(
                    reservacionDTO.getIdVuelo(),
                    reservacionDTO.getAsientoAsignado(),
                    EstadoReserva.CANCELADA
            );
            if (!existentes.isEmpty()) {
                throw new IllegalArgumentException("El asiento " + reservacionDTO.getAsientoAsignado() + " ya esta ocupado en este vuelo");
            }
        }

        return reservacionRepository.save(reservacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservacion obtenerPorId(Long id) {
        return reservacionRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservacion no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservacion> listarPorPasajero(Long idPasajero) {
        return reservacionRepository.findByPasajeroWithRelations(idPasajero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservacion> listarTodas() {
        return reservacionRepository.findAllWithRelations();
    }

    @Override
    public void cancelarReservacion(Long id) {
        Reservacion reservacion = obtenerPorId(id);
        if (reservacion.getEstado_reserva() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("La reservacion ya esta cancelada");
        }
        if (reservacion.getEstado_reserva() == EstadoReserva.CONFIRMADA) {
            throw new IllegalArgumentException("No se puede cancelar una reservacion ya confirmada");
        }
        reservacion.setEstado_reserva(EstadoReserva.CANCELADA);
        reservacionRepository.save(reservacion);
    }

    @Override
    public void confirmarReserva(Long idReservacion) {
        Reservacion reservacion = obtenerPorId(idReservacion);
        if (reservacion.getEstado_reserva() == EstadoReserva.CONFIRMADA) {
            throw new IllegalArgumentException("La reservacion ya esta confirmada");
        }
        if (reservacion.getEstado_reserva() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("No se puede confirmar una reservacion cancelada");
        }
        reservacion.setEstado_reserva(EstadoReserva.CONFIRMADA);
        reservacionRepository.save(reservacion);
    }
}
