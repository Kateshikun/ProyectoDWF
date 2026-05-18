package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Pago;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("SELECT p FROM Pago p " +
           "LEFT JOIN FETCH p.reservacion r " +
           "LEFT JOIN FETCH r.vuelo v " +
           "LEFT JOIN FETCH v.ruta ru " +
           "LEFT JOIN FETCH ru.origen " +
           "LEFT JOIN FETCH ru.destino " +
           "LEFT JOIN FETCH r.pasajero " +
           "WHERE p.reservacion.id_reservacion = :idReservacion")
    Optional<Pago> findByReservacionWithRelations(@Param("idReservacion") Long idReservacion);

    @Query("SELECT p FROM Pago p " +
           "LEFT JOIN FETCH p.reservacion r " +
           "LEFT JOIN FETCH r.vuelo v " +
           "LEFT JOIN FETCH v.ruta ru " +
           "LEFT JOIN FETCH ru.origen " +
           "LEFT JOIN FETCH ru.destino " +
           "LEFT JOIN FETCH r.pasajero " +
           "ORDER BY p.fecha_pago DESC")
    List<Pago> findAllWithRelations();

    @Query("SELECT COALESCE(SUM(p.monto), 0.0) FROM Pago p")
    Double calcularIngresosTotales();
}
