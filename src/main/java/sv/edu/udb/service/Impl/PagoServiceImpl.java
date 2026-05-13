package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.model.Pago;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.repository.PagoRepository;
import sv.edu.udb.repository.ReservacionRepository;
import sv.edu.udb.service.PagoService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ReservacionRepository reservacionRepository;


    @Override
    public Pago procesarPago(PagoDTO pagoDTO) {
        System.out.println("Procesando pago para reservación ID: " + pagoDTO.getIdReservacion() + ", monto: " + pagoDTO.getMonto());
        
        // Validar que el monto sea positivo
        if (pagoDTO.getMonto() <= 0) {
            System.out.println("Intento de pago con monto inválido: " + pagoDTO.getMonto());
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }
        
        // Buscar la reservación
        Reservacion reservacion = reservacionRepository.findById(pagoDTO.getIdReservacion())
                .orElseThrow(() -> {
                    System.out.println("Intento de pago para reservación inexistente ID: " + pagoDTO.getIdReservacion());
                    return new IllegalArgumentException("Reservación no encontrada con ID: " + pagoDTO.getIdReservacion());
                });
        
        // Verificar si ya existe un pago para esta reservación
        Optional<Pago> pagoExistente = pagoRepository.findByReservacionId_reservacion(pagoDTO.getIdReservacion());
        if (pagoExistente.isPresent()) {
            System.out.println("Intento de pago duplicado para reservación ID: " + pagoDTO.getIdReservacion());
            throw new IllegalArgumentException("Ya existe un pago para esta reservación");
        }
        
        // Crear y guardar el pago
        Pago pago = new Pago();
        pago.setReservacion(reservacion);
        pago.setMonto(pagoDTO.getMonto());
        
        Pago pagoGuardado = pagoRepository.save(pago);
        System.out.println("Pago procesado exitosamente con ID: " + pagoGuardado.getIdPago() + " para reservación ID: " + pagoDTO.getIdReservacion());
        
        return pagoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public Pago consultarPagoPorReserva(Long idReservacion) {
        System.out.println("Consultando pago para reservación ID: " + idReservacion);
        
        Optional<Pago> pago = pagoRepository.findByReservacionId_reservacion(idReservacion);
        
        if (pago.isPresent()) {
            System.out.println("Pago encontrado para reservación ID: " + idReservacion + ", pago ID: " + pago.get().getIdPago());
        } else {
            System.out.println("No se encontró pago para reservación ID: " + idReservacion);
        }
        
        return pago.orElse(null);
    }
}
