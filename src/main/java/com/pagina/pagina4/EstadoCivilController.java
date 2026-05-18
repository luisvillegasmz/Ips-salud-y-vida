package com.pagina.pagina4;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/catalogos/estado-civil")
public class EstadoCivilController {

    @Autowired
    private EstadoCivilService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estados", service.listarTodos());
        model.addAttribute("estadoCivil", new EstadoCivil());
        return "HU11-13/estadosCiviles"; // ✅ Ruta corregida
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("estados", service.listarTodos());
        model.addAttribute("estadoCivil", service.buscarPorId(id).orElseThrow());
        return "HU11-13/estadosCiviles"; // ✅ Ruta corregida
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("estadoCivil") EstadoCivil estadoCivil,
                          BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("estados", service.listarTodos());
            return "HU11-13/estadosCiviles";
        }
        try {
            service.guardar(estadoCivil);
            ra.addFlashAttribute("exito", "Estado civil guardado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/catalogos/estado-civil";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        service.cambiarEstado(id);
        return "redirect:/catalogos/estado-civil";
    }

    // ✅ AC-9 HU-013: endpoint REST para Postman
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<EstadoCivil>> apiListar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}