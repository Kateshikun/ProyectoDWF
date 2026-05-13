package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Reservacion;

import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<Reservacion, Long> {

    @Query("SELECT r FROM Reservacion r WHERE r.pasajero.id_pasajero = :idPasajero")
    List<Reservacion> findByPasajeroIdPasajero(@Param("idPasajero") Long idPasajero);
    
    //Cuenta el total de reservaciones
    long count();
    
    /**
     * Obtiene las rutas más demandadas con su cantidad de reservaciones
     * retorna un mapa con la ruta (origen-destino) como clave y cantidad de reservaciones como valor
     */
    @Query("SELECT CONCAT(v.ruta.origen, '-', v.ruta.destino) as ruta, COUNT(r) as cantidad " +
           "FROM Reservacion r JOIN r.vuelo v " +
           "GROUP BY v.ruta.origen, v.ruta.destino " +
           "ORDER BY cantidad DESC")
    List<Object[]> obtenerVuelosMasDemandados();
}
