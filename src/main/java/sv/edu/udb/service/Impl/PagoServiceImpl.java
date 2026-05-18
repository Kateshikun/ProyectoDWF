package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.PagoDTO;
import sv.edu.udb.dto.response.PagoResponse;
import sv.edu.udb.model.Pago;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.repository.PagoRepository;
import sv.edu.udb.repository.ReservacionRepository;
import sv.edu.udb.service.PagoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ReservacionRepository reservacionRepository;

    @Override
    public Pago procesarPago(PagoDTO pagoDTO) {
        if (pagoDTO.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        Reservacion reservacion = reservacionRepository.findById(pagoDTO.getIdReservacion())
                .orElseThrow(() -> new IllegalArgumentException("Reservacion no encontrada con ID: " + pagoDTO.getIdReservacion()));

        pagoRepository.findByReservacionWithRelations(pagoDTO.getIdReservacion())
                .ifPresent(p -> { throw new IllegalArgumentException("Ya existe un pago para esta reservacion"); });

        Pago pago = new Pago();
        pago.setReservacion(reservacion);
        pago.setMonto(pagoDTO.getMonto());

        return pagoRepository.save(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse consultarPagoPorReserva(Long idReservacion) {
        return pagoRepository.findByReservacionWithRelations(idReservacion)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarTodos() {
        return pagoRepository.findAllWithRelations().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PagoResponse mapToResponse(Pago pago) {
        return PagoResponse.builder()
                .idPago(pago.getIdPago())
                .idReservacion(pago.getReservacion() != null ? pago.getReservacion().getId_reservacion() : null)
                .monto(pago.getMonto())
                .fecha_pago(pago.getFecha_pago())
                .build();
    }
}
