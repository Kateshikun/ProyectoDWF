package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reservacion")
    private Reservacion reservacion;

    @NotNull
    @DecimalMin("1.0")
    private Double monto;

    private LocalDateTime fecha_pago = LocalDateTime.now();
}
