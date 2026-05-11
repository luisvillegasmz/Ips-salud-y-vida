package com.pagina.pagina4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PizzaController {
    private final PizzaService pizzaService;
    private final UsuarioService usuarioService;
    private final CarritoService carritoService;
    
    public PizzaController(PizzaService pizzaService, UsuarioService usuarioService, 
                          CarritoService carritoService) {
        this.pizzaService = pizzaService;
        this.usuarioService = usuarioService;
        this.carritoService = carritoService;
    }
    
    // Página de inicio
    @GetMapping("/")
    public String home() {
        return "redirect:/inicioSesion";
    }
    
    // Login
    @GetMapping("/inicioSesion")
    public String mostrarLogin() {
        return "inicioSesion";
    }
    
    @PostMapping("/inicioSesion")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(email, password);
            if (usuario != null) {
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                return "redirect:/seleccion";
            } else {
                redirectAttributes.addFlashAttribute("error", "Email o contraseña incorrectos");
                return "redirect:/inicioSesion";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "redirect:/inicioSesion";
        }
    }
    
    // Registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String nombre,
                                  @RequestParam String apellido,
                                  @RequestParam String email,
                                  @RequestParam String direccion,
                                  @RequestParam String celular,
                                  @RequestParam String password,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setDireccion(direccion);
            usuario.setCelular(celular);
            usuario.setPassword(password);
            
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(usuario);
            session.setAttribute("usuario", usuarioRegistrado);
            session.setAttribute("usuarioId", usuarioRegistrado.getId());
            return "redirect:/seleccion";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
    
    // Logout
    @PostMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/inicioSesion";
    }
    
    // Selección de pizzas
    @GetMapping("/seleccion")
    public String mostrarSeleccion(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        model.addAttribute("pizzasAgrupadas", pizzaService.listarPizzasAgrupadas());
        model.addAttribute("cantidadCarrito", carritoService.calcularCantidadTotal(usuarioId));
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "seleccion";
    }
    
    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long pizzaId,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        carritoService.agregarAlCarrito(usuarioId, pizzaId, cantidad);
        return "redirect:/seleccion";
    }
    
    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        List<ItemCarrito> items = carritoService.obtenerCarritoUsuario(usuarioId);
        Double total = carritoService.calcularTotalCarrito(usuarioId);
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cantidadCarrito", carritoService.calcularCantidadTotal(usuarioId));
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "carrito";
    }
    
    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(@RequestParam Long pizzaId,
                                     @RequestParam int cantidad,
                                     HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        carritoService.actualizarCantidad(usuarioId, pizzaId, cantidad);
        return "redirect:/carrito";
    }
    
    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Long pizzaId, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        carritoService.eliminarDelCarrito(usuarioId, pizzaId);
        return "redirect:/carrito";
    }
    
    @PostMapping("/carrito/vaciar")
    public String vaciarCarrito(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        carritoService.vaciarCarrito(usuarioId);
        return "redirect:/carrito";
    }
    
    @GetMapping("/pago")
    public String mostrarPago(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        List<ItemCarrito> items = carritoService.obtenerCarritoUsuario(usuarioId);
        Double total = carritoService.calcularTotalCarrito(usuarioId);
        
        if (items.isEmpty()) {
            return "redirect:/carrito";
        }
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "pago";
    }
    
    @PostMapping("/procesar-pago")
    public String procesarPago(@RequestParam String metodoPago,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/inicioSesion";
        }
        
        try {
            // Obtener datos del carrito
            List<ItemCarrito> items = carritoService.obtenerCarritoUsuario(usuarioId);
            Double total = carritoService.calcularTotalCarrito(usuarioId);
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            if (items.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El carrito está vacío");
                return "redirect:/carrito";
            }
            
            // Preparar datos para la confirmación
            LocalDateTime fecha = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String numeroOrden = "ORD-" + System.currentTimeMillis();
            
            model.addAttribute("items", items);
            model.addAttribute("total", total);
            model.addAttribute("metodoPago", metodoPago);
            model.addAttribute("usuario", usuario);
            model.addAttribute("fecha", fecha.format(formatter));
            model.addAttribute("numeroOrden", numeroOrden);
            
            // Vaciar carrito después de confirmar
            carritoService.vaciarCarrito(usuarioId);
            
            return "confirmacion";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pago";
        }
    }
}