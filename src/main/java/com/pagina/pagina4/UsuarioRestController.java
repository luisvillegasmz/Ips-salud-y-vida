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

    // AC-9: Crear usuario desde Postman con JSON
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Listar desde Postman
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    // Inactivar desde Postman
    @PatchMapping("/inactivar/{id}")
    public ResponseEntity<String> inactivar(@PathVariable Long id) {
        usuarioService.inactivarUsuario(id);
        return ResponseEntity.ok("Usuario inactivado correctamente.");
    }
}
