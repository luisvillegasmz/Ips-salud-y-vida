package com.pagina.pagina4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
    logger.info("Intentando registrar usuario: {}", usuario.getEmail());

    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
        logger.warn("Email ya registrado: {}", usuario.getEmail());
        throw new RuntimeException("El email ya está registrado.");
    }
    if (usuarioRepository.existsByUsername(usuario.getUsername())) {
        logger.warn("Username ya registrado: {}", usuario.getUsername());
        throw new RuntimeException("El nombre de usuario ya está en uso.");
    }

    usuario.setActivo(true);
    Usuario guardado = usuarioRepository.save(usuario);
    logger.info("Usuario guardado con ID: {}", guardado.getId());
    return guardado;
}

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        Usuario existente = obtenerPorId(id);
        existente.setNombre(datosNuevos.getNombre());
        existente.setApellido(datosNuevos.getApellido());
        existente.setDireccion(datosNuevos.getDireccion());
        existente.setCelular(datosNuevos.getCelular());
        return usuarioRepository.save(existente);
    }

    public void inactivarUsuario(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .filter(Usuario::getActivo)
                .orElse(null);
    }
}
