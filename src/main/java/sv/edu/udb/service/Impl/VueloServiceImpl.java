package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.VueloDto;
import sv.edu.udb.dto.response.VueloResponseDto;
import sv.edu.udb.model.Avion;
import sv.edu.udb.model.Ruta;
import sv.edu.udb.model.Vuelo;
import sv.edu.udb.model.enums.EstadoVuelo;
import sv.edu.udb.repository.AvionRepository;
import sv.edu.udb.repository.RutaRepository;
import sv.edu.udb.repository.VueloRepository;
import sv.edu.udb.service.VueloService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VueloServiceImpl implements VueloService {

    private final VueloRepository vueloRepository;
    private final RutaRepository rutaRepository;
    private final AvionRepository avionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VueloResponseDto> listarTodos() {
        List<Vuelo> vuelos = vueloRepository.findAllWithRelations();
        return vuelos.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VueloResponseDto obtenerPorId(Long id) {
        Vuelo vuelo = vueloRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + id));
        return mapToResponseDto(vuelo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VueloResponseDto> buscarVuelos(String origen, String destino, LocalDateTime fecha) {
        LocalDateTime inicioDia = fecha.with(LocalTime.MIN);
        LocalDateTime finDia = fecha.with(LocalTime.MAX);
        List<Vuelo> vuelos = vueloRepository.buscarPorRutaYFecha(origen, destino, inicioDia, finDia);
        return vuelos.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public Vuelo guardar(VueloDto vueloDto) {
        Ruta ruta = rutaRepository.findById(vueloDto.getId_ruta())
                .orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada con ID: " + vueloDto.getId_ruta()));

        Avion avion = avionRepository.findById(vueloDto.getId_avion())
                .orElseThrow(() -> new IllegalArgumentException("Avion no encontrado con ID: " + vueloDto.getId_avion()));

        if (vueloDto.getFecha_salida().isAfter(vueloDto.getFecha_llegada())) {
            throw new IllegalArgumentException("La fecha de salida debe ser anterior a la fecha de llegada");
        }

        Vuelo vuelo;
        if (vueloDto.getId_vuelo() != null) {
            vuelo = obtenerEntidadPorId(vueloDto.getId_vuelo());
        } else {
            vuelo = new Vuelo();
        }

        vuelo.setRuta(ruta);
        vuelo.setAvion(avion);
        vuelo.setFecha_salida(vueloDto.getFecha_salida());
        vuelo.setFecha_llegada(vueloDto.getFecha_llegada());
        vuelo.setEstado(EstadoVuelo.fromString(vueloDto.getEstado()));
        vuelo.setNombre_piloto(vueloDto.getNombre_piloto());
        vuelo.setNombre_copiloto(vueloDto.getNombre_copiloto());

        if (vuelo.getEstado() == null) {
            vuelo.setEstado(EstadoVuelo.PROGRAMADO);
        }

        return vueloRepository.save(vuelo);
    }

    @Override
    public void eliminar(Long id) {
        obtenerEntidadPorId(id);
        vueloRepository.deleteById(id);
    }

    @Override
    public Vuelo actualizarEstado(Long id, String nuevoEstado) {
        EstadoVuelo estado = EstadoVuelo.fromString(nuevoEstado);
        Vuelo vuelo = obtenerEntidadPorId(id);
        vuelo.setEstado(estado);
        return vueloRepository.save(vuelo);
    }

    private Vuelo obtenerEntidadPorId(Long id) {
        return vueloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + id));
    }

    private VueloResponseDto mapToResponseDto(Vuelo vuelo) {
        return VueloResponseDto.builder()
                .id_vuelo(vuelo.getId_vuelo())
                .id_ruta(vuelo.getRuta() != null ? vuelo.getRuta().getId_ruta() : null)
                .origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null
                        ? vuelo.getRuta().getOrigen().getNombre() : null)
                .ciudad_origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null
                        ? vuelo.getRuta().getOrigen().getCiudad() : null)
                .pais_origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null
                        ? vuelo.getRuta().getOrigen().getPais() : null)
                .destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null
                        ? vuelo.getRuta().getDestino().getNombre() : null)
                .ciudad_destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null
                        ? vuelo.getRuta().getDestino().getCiudad() : null)
                .pais_destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null
                        ? vuelo.getRuta().getDestino().getPais() : null)
                .distancia_km(vuelo.getRuta() != null ? vuelo.getRuta().getDistancia_km() : null)
                .id_avion(vuelo.getAvion() != null ? vuelo.getAvion().getId_avion() : null)
                .capacidad_pasajeros(vuelo.getAvion() != null ? vuelo.getAvion().getCapacidad_pasajeros() : null)
                .fecha_salida(vuelo.getFecha_salida())
                .fecha_llegada(vuelo.getFecha_llegada())
                .estado(vuelo.getEstado() != null ? vuelo.getEstado().getLabel() : null)
                .nombre_piloto(vuelo.getNombre_piloto())
                .nombre_copiloto(vuelo.getNombre_copiloto())
                .build();
    }
}
