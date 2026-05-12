package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    
    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        // Proteger la ruta: si no hay sesión, redirigir al login
        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        if (usuario == null) {
            return "redirect:/inicioSesion";
        }
        model.addAttribute("usuario", usuario);
        return "dashboard"; // src/main/resources/templates/dashboard.html
    }
}
