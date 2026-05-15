package com.pagina.pagina4;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/inicioSesion")
    public String mostrarLogin() {
        return "HU01-03/inicioSesion"; 
    }

    @PostMapping("/inicioSesion")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        logger.info("Intento de inicio de sesión para el email: {}", email);
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            logger.warn("Intento de login con campos vacíos.");
            model.addAttribute("error", "Por favor, completa todos los campos.");
            return "HU01-03/inicioSesion"; 
        }
        Usuario usuario = usuarioService.autenticarUsuario(email, password);
        if (usuario != null) {
            session.setAttribute("usuarioActivo", usuario);
            session.setAttribute("nombreUsuario", usuario.getNombre());
            logger.info("Login exitoso para: {} (ID: {})", email, usuario.getId());
            return "redirect:/dashboard";
        } else {
            logger.warn("Credenciales inválidas para el email: {}", email);
            model.addAttribute("error", "Email o contraseña incorrectos. Intenta de nuevo.");
            return "HU01-03/inicioSesion";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        String nombre = session.getAttribute("nombreUsuario") != null
                ? session.getAttribute("nombreUsuario").toString() : "desconocido";
        logger.info("Cierre de sesión del usuario: {}", nombre);
        session.invalidate();
        return "redirect:/inicioSesion";
    }
}