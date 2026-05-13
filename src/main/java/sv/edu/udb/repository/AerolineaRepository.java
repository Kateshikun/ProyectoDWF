package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Aerolinea;

@Repository
public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
}
