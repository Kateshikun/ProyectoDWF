package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Aeropuerto;

@Repository
public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long> {
}
