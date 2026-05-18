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
@RequestMapping("/catalogos/genero")
public class GeneroController {

    @Autowired
    private GeneroService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("generos", service.listarTodos());
        model.addAttribute("genero", new Genero());
        return "HU11-13/Generos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("generos", service.listarTodos());
        model.addAttribute("genero", service.buscarPorId(id).orElseThrow());
        return "HU11-13/Generos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("genero") Genero genero,
                          BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("generos", service.listarTodos());
            return "HU11-13/Generos";
        }
        try {
            service.guardar(genero);
            ra.addFlashAttribute("exito", "Género guardado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/catalogos/genero";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        service.cambiarEstado(id);
        return "redirect:/catalogos/genero";
    }

    // ✅ AC-7 y AC-9 HU-012: endpoint REST JSON para módulo de pacientes y Postman
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Genero>> apiListar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> apiGuardar(@RequestBody Genero genero) {
        try {
            return ResponseEntity.ok(service.guardar(genero));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}