package com.pagina.pagina4;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @PatchMapping("/inactivar/{id}")
    public ResponseEntity<String> inactivar(@PathVariable Long id) {
        usuarioService.inactivarUsuario(id);
        return ResponseEntity.ok("Usuario inactivado correctamente.");
    }
}
