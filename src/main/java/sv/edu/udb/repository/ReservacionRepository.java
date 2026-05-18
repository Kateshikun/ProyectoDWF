package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Reservacion;
import sv.edu.udb.model.enums.EstadoReserva;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservacionRepository extends JpaRepository<Reservacion, Long> {

    @Query("SELECT r FROM Reservacion r " +
           "LEFT JOIN FETCH r.vuelo v " +
           "LEFT JOIN FETCH v.ruta ru " +
           "LEFT JOIN FETCH ru.origen " +
           "LEFT JOIN FETCH ru.destino " +
           "LEFT JOIN FETCH r.pasajero " +
           "WHERE r.pasajero.id_pasajero = :idPasajero " +
           "ORDER BY r.fecha_reservacion DESC")
    List<Reservacion> findByPasajeroWithRelations(@Param("idPasajero") Long idPasajero);

    @Query("SELECT r FROM Reservacion r " +
           "LEFT JOIN FETCH r.vuelo v " +
           "LEFT JOIN FETCH v.ruta ru " +
           "LEFT JOIN FETCH ru.origen " +
           "LEFT JOIN FETCH ru.destino " +
           "LEFT JOIN FETCH r.pasajero " +
           "ORDER BY r.fecha_reservacion DESC")
    List<Reservacion> findAllWithRelations();

    @Query("SELECT r FROM Reservacion r " +
           "LEFT JOIN FETCH r.vuelo v " +
           "LEFT JOIN FETCH v.ruta ru " +
           "LEFT JOIN FETCH ru.origen " +
           "LEFT JOIN FETCH ru.destino " +
           "LEFT JOIN FETCH r.pasajero " +
           "WHERE r.id_reservacion = :id")
    Optional<Reservacion> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT CONCAT(v.ruta.origen, '-', v.ruta.destino) as ruta, COUNT(r) as cantidad " +
           "FROM Reservacion r JOIN r.vuelo v " +
           "GROUP BY v.ruta.origen, v.ruta.destino " +
           "ORDER BY cantidad DESC")
    List<Object[]> obtenerVuelosMasDemandados();

    @Query("SELECT r FROM Reservacion r WHERE r.vuelo.id_vuelo = :idVuelo " +
            "AND r.asiento_asignado = :asiento " +
            "AND r.estado_reserva <> :estado")
    List<Reservacion> buscarAsientoOcupado(
            @Param("idVuelo") Long idVuelo,
            @Param("asiento") String asiento,
            @Param("estado") EstadoReserva estado
    );
}
