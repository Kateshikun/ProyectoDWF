package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {

    @Query("SELECT v FROM Vuelo v " +
           "LEFT JOIN FETCH v.ruta r " +
           "LEFT JOIN FETCH r.origen " +
           "LEFT JOIN FETCH r.destino " +
           "LEFT JOIN FETCH v.avion " +
           "ORDER BY v.fecha_salida DESC")
    List<Vuelo> findAllWithRelations();

    @Query("SELECT v FROM Vuelo v " +
           "LEFT JOIN FETCH v.ruta r " +
           "LEFT JOIN FETCH r.origen " +
           "LEFT JOIN FETCH r.destino " +
           "LEFT JOIN FETCH v.avion " +
           "WHERE v.id_vuelo = :id")
    Optional<Vuelo> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT v FROM Vuelo v " +
           "LEFT JOIN FETCH v.ruta r " +
           "LEFT JOIN FETCH r.origen " +
           "LEFT JOIN FETCH r.destino " +
           "LEFT JOIN FETCH v.avion " +
           "WHERE r.origen.ciudad = :origen AND r.destino.ciudad = :destino " +
           "AND v.fecha_salida BETWEEN :inicio AND :fin " +
           "ORDER BY v.fecha_salida ASC")
    List<Vuelo> buscarPorRutaYFecha(
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("inicio") LocalDateTime inicioDia,
            @Param("fin") LocalDateTime finDia
    );
}
