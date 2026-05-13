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
public class VueloServiceImpl implements VueloService {

    private final VueloRepository vueloRepository;
    private final RutaRepository rutaRepository;
    private final AvionRepository avionRepository;


    @Override
    @Transactional(readOnly = true)
    public List<VueloResponseDto> listarTodos() {
        System.out.println("Listando todos los vuelos");
        
        List<Vuelo> vuelos = vueloRepository.findAll();
        System.out.println("Se encontraron " + vuelos.size() + " vuelos en el sistema");
        
        return vuelos.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public VueloResponseDto obtenerPorId(Long id) {
        System.out.println("Buscando vuelo por ID: " + id);
        
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Vuelo no encontrado con ID: " + id);
                    return new IllegalArgumentException("Vuelo no encontrado con ID: " + id);
                });
        
        return mapToResponseDto(vuelo);
    }


    @Override
    @Transactional(readOnly = true)
    public List<VueloResponseDto> buscarVuelos(String origen, String destino, LocalDateTime fecha) {
        System.out.println("Buscando vuelos de " + origen + " a " + destino + " para la fecha: " + fecha.toLocalDate());
        
        // Definir el rango de búsqueda para el día completo
        LocalDateTime inicioDia = fecha.with(LocalTime.MIN);
        LocalDateTime finDia = fecha.with(LocalTime.MAX);
        
        List<Vuelo> vuelos = vueloRepository.buscarPorRutaYFecha(
                origen, destino, inicioDia, finDia);
        
        System.out.println("Se encontraron " + vuelos.size() + " vuelos para los criterios especificados");
        
        return vuelos.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public Vuelo guardar(VueloDto vueloDto) {
        System.out.println("Guardando vuelo con ruta ID: " + vueloDto.getId_ruta() + ", avión ID: " + vueloDto.getId_avion());
        
        // Validar que la ruta exista
        Ruta ruta = rutaRepository.findById(vueloDto.getId_ruta())
                .orElseThrow(() -> {
                    System.out.println("Intento de guardar vuelo con ruta inexistente ID: " + vueloDto.getId_ruta());
                    return new IllegalArgumentException("Ruta no encontrada con ID: " + vueloDto.getId_ruta());
                });
        
        // Validar que el avión exista
        Avion avion = avionRepository.findById(vueloDto.getId_avion())
                .orElseThrow(() -> {
                    System.out.println("Intento de guardar vuelo con avión inexistente ID: " + vueloDto.getId_avion());
                    return new IllegalArgumentException("Avión no encontrado con ID: " + vueloDto.getId_avion());
                });
        
        // Validar fechas
        if (vueloDto.getFecha_salida().isAfter(vueloDto.getFecha_llegada())) {
            System.out.println("Fecha de salida posterior a fecha de llegada");
            throw new IllegalArgumentException("La fecha de salida debe ser anterior a la fecha de llegada");
        }
        
        // Crear o actualizar el vuelo
        Vuelo vuelo;
        if (vueloDto.getId_vuelo() != null) {
            // Actualización
            vuelo = obtenerEntidadPorId(vueloDto.getId_vuelo());
            System.out.println("Actualizando vuelo existente ID: " + vueloDto.getId_vuelo());
        } else {
            // Nuevo vuelo
            vuelo = new Vuelo();
            System.out.println("Creando nuevo vuelo");
        }
        
        // Asignar propiedades
        vuelo.setRuta(ruta);
        vuelo.setAvion(avion);
        vuelo.setFecha_salida(vueloDto.getFecha_salida());
        vuelo.setFecha_llegada(vueloDto.getFecha_llegada());
        vuelo.setEstado(vueloDto.getEstado());
        vuelo.setNombre_piloto(vueloDto.getNombre_piloto());
        vuelo.setNombre_copiloto(vueloDto.getNombre_copiloto());
        
        // Establecer estado por defecto si no se especifica
        if (vuelo.getEstado() == null || vuelo.getEstado().isEmpty()) {
            vuelo.setEstado("Programado");
        }
        
        Vuelo vueloGuardado = vueloRepository.save(vuelo);
        System.out.println("Vuelo guardado exitosamente con ID: " + vueloGuardado.getId_vuelo());
        
        return vueloGuardado;
    }


    @Override
    public void eliminar(Long id) {
        System.out.println("Eliminando vuelo ID: " + id);
        
        // Verificar que el vuelo exista
        obtenerEntidadPorId(id);
        
        // TODO: Validar que no existan reservaciones asociadas antes de eliminar
        
        vueloRepository.deleteById(id);
        System.out.println("Vuelo ID: " + id + " eliminado exitosamente");
    }


    @Override
    public Vuelo actualizarEstado(Long id, String nuevoEstado) {
        System.out.println("Actualizando estado del vuelo ID: " + id + " a: " + nuevoEstado);
        
        // Validar que el estado sea válido
        if (!nuevoEstado.matches("Programado|Retrasado|Cancelado")) {
            System.out.println("Estado de vuelo inválido: " + nuevoEstado);
            throw new IllegalArgumentException("Estado inválido. Debe ser: Programado, Retrasado o Cancelado");
        }
        
        Vuelo vuelo = obtenerEntidadPorId(id);
        vuelo.setEstado(nuevoEstado);
        
        Vuelo vueloActualizado = vueloRepository.save(vuelo);
        System.out.println("Estado del vuelo ID: " + id + " actualizado exitosamente a: " + nuevoEstado);
        
        return vueloActualizado;
    }

// Metodos de apoyo

    private Vuelo obtenerEntidadPorId(Long id) {
        return vueloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + id));
    }

//Tiene muchas expresiones if para verificar si esta presente cierta informacion, y si no guardarlo como null
    private VueloResponseDto mapToResponseDto(Vuelo vuelo) {
        return VueloResponseDto.builder()
                .id_vuelo(vuelo.getId_vuelo())
                // Datos de la ruta
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
                // Datos del avión
                .id_avion(vuelo.getAvion() != null ? vuelo.getAvion().getId_avion() : null)
                .capacidad_pasajeros(vuelo.getAvion() != null ? vuelo.getAvion().getCapacidad_pasajeros() : null)
                // Datos de la aerolínea
                .id_aerolinea(vuelo.getAvion() != null && vuelo.getAvion().getAerolinea() != null 
                        ? vuelo.getAvion().getAerolinea().getIdAerolinea() : null)
                .nombre_aerolinea(vuelo.getAvion() != null && vuelo.getAvion().getAerolinea() != null 
                        ? vuelo.getAvion().getAerolinea().getNombre() : null)
                .codigo_icao(vuelo.getAvion() != null && vuelo.getAvion().getAerolinea() != null 
                        ? vuelo.getAvion().getAerolinea().getCodigo_icao() : null)
                // Datos del vuelo
                .fecha_salida(vuelo.getFecha_salida())
                .fecha_llegada(vuelo.getFecha_llegada())
                .estado(vuelo.getEstado())
                .nombre_piloto(vuelo.getNombre_piloto())
                .nombre_copiloto(vuelo.getNombre_copiloto())
                .build();
    }
}
