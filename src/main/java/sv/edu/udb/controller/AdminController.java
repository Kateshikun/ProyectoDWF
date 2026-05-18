package sv.edu.udb.controller;

import jakarta.validation.Valid;
import sv.edu.udb.dto.request.UsuarioDTO;
import sv.edu.udb.dto.response.UsuarioResponse;
import sv.edu.udb.service.ReporteService;
import sv.edu.udb.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administracion", description = "Endpoints exclusivos para el rol de Administrador")
public class AdminController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener el total de ingresos generados por ventas")
    @GetMapping("/reportes/ingresos")
    public ResponseEntity<Double> obtenerIngresosTotales() {
        Double ingresos = reporteService.calcularIngresosTotales();
        return ResponseEntity.ok(ingresos != null ? ingresos : 0.0);
    }

    @Operation(summary = "Obtener las rutas de vuelo con mayor demanda")
    @GetMapping("/reportes/demanda")
    public ResponseEntity<Map<String, Long>> obtenerDemandaDeVuelos() {
        return ResponseEntity.ok(reporteService.obtenerVuelosMasDemandados());
    }

    @Operation(summary = "Obtener el total de reservaciones registradas")
    @GetMapping("/reportes/reservas-totales")
    public ResponseEntity<Long> obtenerTotalReservas() {
        return ResponseEntity.ok(reporteService.contarReservasTotales());
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @Operation(summary = "Cambiar el estado de un usuario")
    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<String> cambiarEstadoUsuario(@PathVariable Long id, @RequestParam boolean activo) {
        usuarioService.cambiarEstadoUsuario(id, activo);
        return ResponseEntity.ok(activo ? "Usuario activado exitosamente" : "Usuario desactivado exitosamente");
    }

    @Operation(summary = "Eliminar un usuario del sistema")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @Operation(summary = "Actualizar datos de un usuario")
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuarioDTO));
    }
}
