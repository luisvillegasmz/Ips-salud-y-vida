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
@RequestMapping("/municipios")
public class MunicipioController {

    private static final Logger logger = LoggerFactory.getLogger(MunicipioController.class);
    private final MunicipioService municipioService;
    private final DepartamentoService departamentoService;

    public MunicipioController(MunicipioService municipioService,
                               DepartamentoService departamentoService) {
        this.municipioService = municipioService;
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String exito,
            Model model) {
        model.addAttribute("municipios", municipioService.listarTodos());
        model.addAttribute("departamentos", departamentoService.listarTodos());
        if (exito != null) model.addAttribute("exito", "Operación realizada correctamente.");
        return "HU16-18/listarMunicipios";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(required = false) Long id,
            @RequestParam String codigoDane,
            @RequestParam String nombre,
            @RequestParam Long departamentoId) {
        Municipio municipio = id != null
                ? municipioService.buscarPorId(id).orElse(new Municipio())
                : new Municipio();
        municipio.setCodigoDane(codigoDane.trim());
        municipio.setNombre(nombre.trim());
        municipio.setDepartamento(municipioService.buscarDepartamentoPorId(departamentoId));
        municipioService.guardar(municipio);
        logger.info("Municipio guardado: {} - Depto ID: {}", nombre, departamentoId);
        return "redirect:/municipios?exito=true";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("editar", municipioService.buscarPorId(id).orElseThrow());
        model.addAttribute("municipios", municipioService.listarTodos());
        model.addAttribute("departamentos", departamentoService.listarTodos());
        return "HU16-18/listarMunicipios";
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Municipio>> apiListar() {
        return ResponseEntity.ok(municipioService.listarTodos());
    }
}