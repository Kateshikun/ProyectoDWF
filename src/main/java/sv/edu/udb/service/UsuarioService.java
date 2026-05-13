package sv.edu.udb.service;

import sv.edu.udb.dto.request.UsuarioDTO;
import sv.edu.udb.model.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(UsuarioDTO usuarioDTO); // Cifra contraseña
    Optional<Usuario> buscarPorUsername(String username);
    boolean existePorEmail(String email);
    void cambiarEstadoUsuario(Long id, boolean activo);
    void eliminarUsuario(Long id);
    Usuario actualizar(Long id, UsuarioDTO usuarioDTO); // Actualizar datos de usuario
}
