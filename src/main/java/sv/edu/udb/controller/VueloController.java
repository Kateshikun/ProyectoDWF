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
@Tag(name = "Vuelos", description = "Endpoints para gestion de vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Operation(summary = "Listar todos los vuelos")
    @GetMapping("/")
    public ResponseEntity<List<VueloResponseDto>> listarTodos() {
        return ResponseEntity.ok(vueloService.listarTodos());
    }

    @Operation(summary = "Buscar vuelos por origen, destino y fecha")
    @GetMapping("/buscar")
    public ResponseEntity<List<VueloResponseDto>> buscarVuelos(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        return ResponseEntity.ok(vueloService.buscarVuelos(origen, destino, fecha));
    }

    @Operation(summary = "Obtener detalle de un vuelo")
    @GetMapping("/{id}")
    public ResponseEntity<VueloResponseDto> obtenerVuelo(@PathVariable Long id) {
        return ResponseEntity.ok(vueloService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un nuevo vuelo")
    @PostMapping("/")
    public ResponseEntity<VueloResponseDto> crearVuelo(@Valid @RequestBody VueloDto vueloDto) {
        Vuelo vueloCreado = vueloService.guardar(vueloDto);
        return ResponseEntity.ok(vueloService.obtenerPorId(vueloCreado.getId_vuelo()));
    }

    @Operation(summary = "Eliminar un vuelo")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVuelo(@PathVariable Long id) {
        vueloService.eliminar(id);
        return ResponseEntity.ok("Vuelo eliminado exitosamente");
    }

    @Operation(summary = "Actualizar estado de un vuelo")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstadoVuelo(@PathVariable Long id, @RequestParam String estado) {
        vueloService.actualizarEstado(id, estado);
        return ResponseEntity.ok("Estado del vuelo actualizado correctamente");
    }
}
