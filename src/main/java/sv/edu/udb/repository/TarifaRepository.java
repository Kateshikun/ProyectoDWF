package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Tarifa;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
}
