package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Pago;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    @Query("SELECT p FROM Pago p WHERE p.reservacion.id_reservacion = :idReservacion")
    Optional<Pago> findByReservacionId_reservacion(@Param("idReservacion") Long idReservacion);
    
//Calcula el total de ingresos sumando todos los montos de los pagos
    @Query("SELECT COALESCE(SUM(p.monto), 0.0) FROM Pago p")
    Double calcularIngresosTotales();
}
