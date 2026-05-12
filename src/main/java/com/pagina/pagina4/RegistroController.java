package com.pagina.pagina4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    private static final Logger logger = LoggerFactory.getLogger(RegistroController.class);
    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String direccion,
            @RequestParam String celular,
            @RequestParam String password,
            Model model) {

        if (nombre.trim().isEmpty() || username.trim().isEmpty() ||
            email.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, completa todos los campos obligatorios.");
            return "registro";
        }

        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setUsername(username);
            nuevo.setEmail(email);
            nuevo.setDireccion(direccion);
            nuevo.setCelular(celular);
            nuevo.setPassword(password);

            usuarioService.registrarUsuario(nuevo);
            logger.info("Usuario registrado: {} ({})", username, email);
            return "redirect:/inicioSesion";

        } catch (RuntimeException e) {
            logger.warn("Error en registro: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}
