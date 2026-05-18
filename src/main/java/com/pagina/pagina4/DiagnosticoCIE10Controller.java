package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cie10")
public class DiagnosticoCIE10Controller {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoCIE10Controller.class);
    private final DiagnosticoCIE10Service service;

    private static final java.util.List<String> CATEGORIAS = java.util.List.of(
            "Respiratorio", "Endocrino", "Cardiovascular", "Neurológico",
            "Gastrointestinal", "Musculoesquelético", "Dermatológico",
            "Genitourinario", "Hematológico", "Psiquiátrico", "Infeccioso", "Otro"
    );

    public DiagnosticoCIE10Controller(DiagnosticoCIE10Service service) {
        this.service = service;
    }

    /* ── LISTADO + BÚSQUEDA ─────────────────────────────────────────── */
    @GetMapping
    public String listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String exito,
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";

        model.addAttribute("diagnosticos", service.buscar(q));
        model.addAttribute("q", q);
        if (exito != null) model.addAttribute("exito", exito);
        return "HU026/catalogoCIE10";
    }

    /* ── FORMULARIO AÑADIR ──────────────────────────────────────────── */
    @GetMapping("/nuevo")
    public String formNuevo(Model model, HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        model.addAttribute("diagnostico", new DiagnosticoCIE10());
        model.addAttribute("categorias", CATEGORIAS);
        model.addAttribute("modoEdicion", false);
        return "HU026/formCIE10";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(
            @RequestParam String codigo,
            @RequestParam String diagnostico,
            @RequestParam String categoria,
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        try {
            DiagnosticoCIE10 d = new DiagnosticoCIE10();
            d.setCodigo(codigo);
            d.setDiagnostico(diagnostico.trim());
            d.setCategoria(categoria);
            service.guardar(d);
            logger.info("Diagnóstico CIE-10 creado: {}", codigo);
            return "redirect:/cie10?exito=Diagnóstico+añadido+exitosamente";
        } catch (RuntimeException e) {
            logger.warn("Error al crear diagnóstico: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categorias", CATEGORIAS);
            model.addAttribute("modoEdicion", false);
            DiagnosticoCIE10 d = new DiagnosticoCIE10();
            d.setCodigo(codigo); d.setDiagnostico(diagnostico); d.setCategoria(categoria);
            model.addAttribute("diagnostico", d);
            return "HU026/formCIE10";
        }
    }

    /* ── FORMULARIO EDITAR ──────────────────────────────────────────── */
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        model.addAttribute("diagnostico", service.obtenerPorId(id));
        model.addAttribute("categorias", CATEGORIAS);
        model.addAttribute("modoEdicion", true);
        return "HU026/formCIE10";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestParam String diagnostico,
            @RequestParam String categoria,
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        try {
            DiagnosticoCIE10 d = service.obtenerPorId(id);
            d.setCodigo(codigo);
            d.setDiagnostico(diagnostico.trim());
            d.setCategoria(categoria);
            service.guardar(d);
            logger.info("Diagnóstico CIE-10 editado: id={}", id);
            return "redirect:/cie10?exito=Diagnóstico+actualizado+exitosamente";
        } catch (RuntimeException e) {
            logger.warn("Error al editar diagnóstico id={}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categorias", CATEGORIAS);
            model.addAttribute("modoEdicion", true);
            DiagnosticoCIE10 d = service.obtenerPorId(id);
            d.setCodigo(codigo); d.setDiagnostico(diagnostico); d.setCategoria(categoria);
            model.addAttribute("diagnostico", d);
            return "HU026/formCIE10";
        }
    }

    /* ELIMINAR  */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        service.eliminar(id);
        logger.info("Diagnóstico CIE-10 eliminado: id={}", id);
        return "redirect:/cie10?exito=Diagnóstico+eliminado";
    }

    /* ── CAMBIAR ESTADO (activar / inactivar) ───────────────────────── */
    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        service.cambiarEstado(id);
        return "redirect:/cie10";
    }
}
