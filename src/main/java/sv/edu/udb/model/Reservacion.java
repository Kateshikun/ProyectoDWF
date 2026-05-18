package sv.edu.udb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import sv.edu.udb.model.enums.EstadoReserva;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVACION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reservacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vuelo")
    private Vuelo vuelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pasajero")
    private Pasajero pasajero;

    private LocalDateTime fecha_reservacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoReserva estado_reserva;

    @Pattern(regexp = "^[A-Z0-9]{2,3}$")
    private String asiento_asignado;
}
