package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {

    // Busca vuelos por ciudad de origen, ciudad de destino y rango de fecha
    @Query("SELECT v FROM Vuelo v WHERE v.ruta.origen.ciudad = :origen AND v.ruta.destino.ciudad = :destino AND v.fecha_salida BETWEEN :inicio AND :fin")
    List<Vuelo> buscarPorRutaYFecha(
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("inicio") LocalDateTime inicioDia,
            @Param("fin") LocalDateTime finDia
    );

}
