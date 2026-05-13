package sv.edu.udb.service;

import org.springframework.stereotype.Service;
import sv.edu.udb.dto.request.VueloDto;
import sv.edu.udb.dto.response.VueloResponseDto;
import sv.edu.udb.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;

public interface VueloService {
    List<VueloResponseDto> listarTodos();
    VueloResponseDto obtenerPorId(Long id);
    List<VueloResponseDto> buscarVuelos(String origen, String destino, LocalDateTime fecha);
    Vuelo guardar(VueloDto vueloDto);
    void eliminar(Long id);
    Vuelo actualizarEstado(Long id, String nuevoEstado);
}
