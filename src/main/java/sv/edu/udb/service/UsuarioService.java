package sv.edu.udb.service;

import sv.edu.udb.dto.request.UsuarioDTO;
import sv.edu.udb.dto.response.UsuarioResponse;
import sv.edu.udb.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(UsuarioDTO usuarioDTO);
    Optional<Usuario> buscarPorUsername(String username);
    boolean existePorEmail(String email);
    void cambiarEstadoUsuario(Long id, boolean activo);
    void eliminarUsuario(Long id);
    UsuarioResponse actualizar(Long id, UsuarioDTO usuarioDTO);
    List<UsuarioResponse> listarTodos();
    UsuarioResponse obtenerPorId(Long id);
}
