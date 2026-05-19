package com.pagina.pagina4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/discapacidades")
public class DiscapacidadController {

    private static final Logger logger = LoggerFactory.getLogger(DiscapacidadController.class);
    private final DiscapacidadService discapacidadService;

    public DiscapacidadController(DiscapacidadService discapacidadService) {
        this.discapacidadService = discapacidadService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String exito, Model model) {
        model.addAttribute("discapacidades", discapacidadService.listarActivas());
        if (exito != null) model.addAttribute("exito", "Operación realizada correctamente.");
        return "HU16-18/listarDiscapacidades";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(required = false) Long id,
            @RequestParam String nombre) {
        Discapacidad discapacidad = id != null
                ? discapacidadService.buscarPorId(id).orElse(new Discapacidad())
                : new Discapacidad();
        discapacidad.setNombre(nombre.trim());
        discapacidadService.guardar(discapacidad);
        logger.info("Discapacidad guardada: {}", nombre);
        return "redirect:/discapacidades?exito=true";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("editar", discapacidadService.buscarPorId(id).orElseThrow());
        model.addAttribute("discapacidades", discapacidadService.listarTodas());
        return "HU16-18/listarDiscapacidades";
    }

    @GetMapping("/inactivar/{id}")
    public String inactivar(@PathVariable Long id) {
        discapacidadService.inactivar(id);
        return "redirect:/discapacidades?exito=true";
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Discapacidad>> apiListar() {
        return ResponseEntity.ok(discapacidadService.listarTodas());
    }
}