package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.ReservacionDTO;
import sv.edu.udb.model.Pasajero;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.model.Vuelo;
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
        System.out.println("Creando nueva reservación para vuelo ID: " + 
                reservacionDTO.getIdVuelo() + 
                ", pasajero ID: " + 
                reservacionDTO.getIdPasajero());
        
        // Convertir DTO a entidad
        Reservacion reservacion = new Reservacion();
        // Necesitamos buscar las entidades completas para establecer las relaciones
        // Por ahora, crearemos objetos básicos con solo los IDs
        Vuelo vuelo = new Vuelo();
        vuelo.setId_vuelo(reservacionDTO.getIdVuelo());
        reservacion.setVuelo(vuelo);
        
        Pasajero pasajero = new Pasajero();
        pasajero.setId_pasajero(reservacionDTO.getIdPasajero());
        reservacion.setPasajero(pasajero);
        
        reservacion.setAsiento_asignado(reservacionDTO.getAsientoAsignado());
        reservacion.setEstado_reserva(reservacionDTO.getEstadoReserva());
        
        // Validaciones básicas
        if (reservacionDTO.getIdVuelo() == null) {
            System.out.println("Intento de crear reservación sin vuelo");
            throw new IllegalArgumentException("La reservación debe estar asociada a un vuelo");
        }
        
        if (reservacionDTO.getIdPasajero() == null) {
            System.out.println("Intento de crear reservación sin pasajero");
            throw new IllegalArgumentException("La reservación debe estar asociada a un pasajero");
        }
        
        // Establecer estado inicial si no está definido
        if (reservacion.getEstado_reserva() == null || reservacion.getEstado_reserva().isEmpty()) {
            reservacion.setEstado_reserva("Pendiente");
        }
        
        // Validar que el asiento no esté ya ocupado para este vuelo
        if (reservacionDTO.getAsientoAsignado() != null && !reservacionDTO.getAsientoAsignado().trim().isEmpty()) {
            List<Reservacion> reservacionesExistentes = reservacionRepository
                    .buscarAsientoOcupado(
                            reservacionDTO.getIdVuelo(), 
                            reservacionDTO.getAsientoAsignado(), 
                            "Cancelada"
                    );
            
            if (!reservacionesExistentes.isEmpty()) {
                System.out.println("Intento de asignar asiento ya ocupado: " + reservacionDTO.getAsientoAsignado() + 
                        " para vuelo ID: " + reservacionDTO.getIdVuelo());
                throw new IllegalArgumentException("El asiento " + reservacionDTO.getAsientoAsignado() + 
                        " ya está ocupado en este vuelo");
            }
        }
        
        Reservacion reservacionGuardada = reservacionRepository.save(reservacion);
        System.out.println("Reservación creada exitosamente con ID: " + reservacionGuardada.getId_reservacion());
        
        return reservacionGuardada;
    }


    @Override
    @Transactional(readOnly = true)
    public Reservacion obtenerPorId(Long id) {
        System.out.println("Buscando reservación por ID: " + id);
        
        return reservacionRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Reservación no encontrada con ID: " + id);
                    return new IllegalArgumentException("Reservación no encontrada con ID: " + id);
                });
    }


    @Override
    @Transactional(readOnly = true)
    public List<Reservacion> listarPorPasajero(Long idPasajero) {
        System.out.println("Listando reservaciones para pasajero ID: " + idPasajero);
        
        List<Reservacion> reservaciones = reservacionRepository.findByPasajeroIdPasajero(idPasajero);
        System.out.println("Se encontraron " + reservaciones.size() + " reservaciones para el pasajero ID: " + idPasajero);
        
        return reservaciones;
    }


    @Override
    public void cancelarReservacion(Long id) {
        System.out.println("Cancelando reservación ID: " + id);
        
        Reservacion reservacion = obtenerPorId(id);
        
        if ("Cancelada".equals(reservacion.getEstado_reserva())) {
            System.out.println("Intento de cancelar reservación ya cancelada ID: " + id);
            throw new IllegalArgumentException("La reservación ya está cancelada");
        }
        
        if ("Confirmada".equals(reservacion.getEstado_reserva())) {
            System.out.println("Intento de cancelar reservación confirmada ID: " + id);
            throw new IllegalArgumentException("No se puede cancelar una reservación ya confirmada");
        }
        
        reservacion.setEstado_reserva("Cancelada");
        reservacionRepository.save(reservacion);
        
        System.out.println("Reservación ID: " + id + " cancelada exitosamente");
    }


    @Override
    public void confirmarReserva(Long idReservacion) {
        System.out.println("Confirmando reservación ID: " + idReservacion);
        
        Reservacion reservacion = obtenerPorId(idReservacion);
        
        if ("Confirmada".equals(reservacion.getEstado_reserva())) {
            System.out.println("Intento de confirmar reservación ya confirmada ID: " + idReservacion);
            throw new IllegalArgumentException("La reservación ya está confirmada");
        }
        
        if ("Cancelada".equals(reservacion.getEstado_reserva())) {
            System.out.println("Intento de confirmar reservación cancelada ID: " + idReservacion);
            throw new IllegalArgumentException("No se puede confirmar una reservación cancelada");
        }
        
        reservacion.setEstado_reserva("Confirmada");
        reservacionRepository.save(reservacion);
        
        System.out.println("Reservación ID: " + idReservacion + " confirmada exitosamente");
    }
}
