package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
}
