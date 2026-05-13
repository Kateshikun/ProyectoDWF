package sv.edu.udb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.dto.request.UsuarioDTO;
import sv.edu.udb.model.Usuario;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.service.UsuarioService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional //Anotacion por si algo falla volver al estado anterior
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);

    @Override
    public Usuario registrar(UsuarioDTO usuarioDTO) {
        System.out.println("Iniciando registro de usuario: " + usuarioDTO.getUsername());
        
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            System.out.println("Intento de registro con email ya existente: " + usuarioDTO.getEmail());
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }
        
        // Verificar si el username ya existe
        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            System.out.println("Intento de registro con username ya existente: " + usuarioDTO.getUsername());
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        
        // Convertir DTO a entidad
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setEstado(usuarioDTO.isActivo() ? "ACTIVO" : "INACTIVO");
        
        // Cifrar la contraseña en hash antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        System.out.println("Usuario registrado exitosamente con ID: " + usuarioGuardado.getId_usuario());
        
        return usuarioGuardado;
    }

    @Override
    // Habilita la anotación @Transactional para métodos de solo lectura
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        System.out.println("Buscando usuario por username: " + username);
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        System.out.println("Verificando existencia de email: " + email);
        return usuarioRepository.existsByEmail(email);
    }


    @Override
    public void cambiarEstadoUsuario(Long id, boolean activo) {
        System.out.println("Cambiando estado de usuario ID: " + id + " a activo: " + activo);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Intento de cambiar estado de usuario inexistente ID: " + id);
                    return new IllegalArgumentException("Usuario no encontrado con ID: " + id);
                });
        
        // Actualizar el campo estado según el valor booleano
        usuario.setEstado(activo ? "ACTIVO" : "INACTIVO");
        usuarioRepository.save(usuario);
        
        System.out.println("Estado de usuario ID: " + id + " cambiado a: " + usuario.getEstado());
    }

    @Override
    public void eliminarUsuario(Long id) {
        System.out.println("Eliminando usuario ID: " + id);
        
        // Verificar que el usuario exista
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Intento de eliminar usuario inexistente ID: " + id);
                    return new IllegalArgumentException("Usuario no encontrado con ID: " + id);
                });

        usuarioRepository.deleteById(id);
        System.out.println("Usuario ID: " + id + " eliminado exitosamente");
    }

    @Override
    public Usuario actualizar(Long id, UsuarioDTO usuarioDTO) {
        System.out.println("Actualizando usuario ID: " + id + " con datos: " + usuarioDTO.getUsername());
        
        // Buscar usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Intento de actualizar usuario inexistente ID: " + id);
                    return new IllegalArgumentException("Usuario no encontrado con ID: " + id);
                });
        
        // Verificar si el nuevo email ya existe en otro usuario
        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            System.out.println("Intento de actualizar con email ya existente: " + usuarioDTO.getEmail());
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }
        
        // Verificar si el nuevo username ya existe en otro usuario
        if (!usuarioExistente.getUsername().equals(usuarioDTO.getUsername()) && 
            usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            System.out.println("Intento de actualizar con username ya existente: " + usuarioDTO.getUsername());
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        
        // Actualizar campos permitidos
        usuarioExistente.setUsername(usuarioDTO.getUsername());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setRol(usuarioDTO.getRol());
        usuarioExistente.setEstado(usuarioDTO.isActivo() ? "ACTIVO" : "INACTIVO");
        
        // Solo actualizar contraseña si se proporciona una nueva
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        System.out.println("Usuario ID: " + id + " actualizado exitosamente");
        
        return usuarioActualizado;
    }
}
