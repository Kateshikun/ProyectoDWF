package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.request.VueloDto;
import sv.edu.udb.dto.response.VueloResponseDto;
import sv.edu.udb.model.Vuelo;
import sv.edu.udb.service.VueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
@Tag(name = "Vuelos", description = "Endpoints para gestión de vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Operation(summary = "Listar todos los vuelos")
    @GetMapping("/")
    public ResponseEntity<List<VueloResponseDto>> listarTodos() {
        List<VueloResponseDto> vuelos = vueloService.listarTodos();
        return ResponseEntity.ok(vuelos);
    }

    @Operation(summary = "Buscar vuelos por origen, destino y fecha")
    @GetMapping("/buscar")
    public ResponseEntity<List<VueloResponseDto>> buscarVuelos(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {

        List<VueloResponseDto> vuelos = vueloService.buscarVuelos(origen, destino, fecha);
        return ResponseEntity.ok(vuelos);
    }

    @Operation(summary = "Obtener detalle de un vuelo específico")
    @GetMapping("/{id}")
    public ResponseEntity<VueloResponseDto> obtenerVuelo(@PathVariable Long id) {
        VueloResponseDto vuelo = vueloService.obtenerPorId(id);
        return ResponseEntity.ok(vuelo);
    }

    @Operation(summary = "Crear un nuevo vuelo")
    @PostMapping("/")
    public ResponseEntity<VueloResponseDto> crearVuelo(@Valid @RequestBody VueloDto vueloDto) {
        Vuelo vueloCreado = vueloService.guardar(vueloDto);
        VueloResponseDto response = convertirAVueloResponseDto(vueloCreado);
        return ResponseEntity.ok(response);
    }

    private VueloResponseDto convertirAVueloResponseDto(Vuelo vuelo) {
        return VueloResponseDto.builder()
                .id_vuelo(vuelo.getId_vuelo())
                .id_ruta(vuelo.getRuta() != null ? vuelo.getRuta().getId_ruta() : null)
                .origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null ? 
                        vuelo.getRuta().getOrigen().getNombre() : null)
                .ciudad_origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null ? 
                        vuelo.getRuta().getOrigen().getCiudad() : null)
                .pais_origen(vuelo.getRuta() != null && vuelo.getRuta().getOrigen() != null ? 
                        vuelo.getRuta().getOrigen().getPais() : null)
                .destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null ? 
                        vuelo.getRuta().getDestino().getNombre() : null)
                .ciudad_destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null ? 
                        vuelo.getRuta().getDestino().getCiudad() : null)
                .pais_destino(vuelo.getRuta() != null && vuelo.getRuta().getDestino() != null ? 
                        vuelo.getRuta().getDestino().getPais() : null)
                .distancia_km(vuelo.getRuta() != null ? vuelo.getRuta().getDistancia_km() : null)
                .id_avion(vuelo.getAvion() != null ? vuelo.getAvion().getId_avion() : null)
                .capacidad_pasajeros(vuelo.getAvion() != null ? vuelo.getAvion().getCapacidad_pasajeros() : null)
                .fecha_salida(vuelo.getFecha_salida())
                .fecha_llegada(vuelo.getFecha_llegada())
                .estado(vuelo.getEstado())
                .nombre_piloto(vuelo.getNombre_piloto())
                .nombre_copiloto(vuelo.getNombre_copiloto())
                .build();
    }

    @Operation(summary = "Eliminar un vuelo")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVuelo(@PathVariable Long id) {
        vueloService.eliminar(id);
        return ResponseEntity.ok("Vuelo eliminado exitosamente");
    }

    @Operation(summary = "Actualizar estado de un vuelo")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstadoVuelo(
            @PathVariable Long id,
            @RequestParam String estado) {
        vueloService.actualizarEstado(id, estado);
        return ResponseEntity.ok("Estado del vuelo actualizado correctamente");
    }
}
