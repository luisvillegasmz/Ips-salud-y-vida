package com.pagina.pagina4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "HU01-03/listarUsuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "HU01-03/editarUsuario";
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicion(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String direccion,
            @RequestParam String celular,
            Model model) {
        try {
            Usuario datos = new Usuario();
            datos.setNombre(nombre);
            datos.setApellido(apellido);
            datos.setDireccion(direccion);
            datos.setCelular(celular);
            usuarioService.actualizarUsuario(id, datos);
            logger.info("Usuario ID {} actualizado.", id);
            return "redirect:/usuarios";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuarioService.obtenerPorId(id));
            return "HU01-03/editarUsuario"; // ✅ CORREGIDO
        }
    }

    @PostMapping("/inactivar/{id}")
    public String inactivarUsuario(@PathVariable Long id) {
        usuarioService.inactivarUsuario(id);
        logger.info("Usuario ID {} inactivado.", id);
        return "redirect:/usuarios";
    }
}