package com.pagina.pagina4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoController.class);
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String exito,
            @RequestParam(required = false) String error,
            Model model) {
        model.addAttribute("departamentos", departamentoService.listarTodos());
        if (exito != null) model.addAttribute("exito", "Operación realizada correctamente.");
        if (error != null) model.addAttribute("error", "El código DANE ya existe.");
        return "HU16-18/listarDepartamentos";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(required = false) Long id,
            @RequestParam String codigoDane,
            @RequestParam String nombre) {
        if (id == null && departamentoService.codigoDaneExiste(codigoDane)) {
            logger.warn("Código DANE duplicado: {}", codigoDane);
            return "redirect:/departamentos?error=true";
        }
        Departamento departamento = id != null
                ? departamentoService.buscarPorId(id).orElse(new Departamento())
                : new Departamento();
        departamento.setCodigoDane(codigoDane.trim());
        departamento.setNombre(nombre.trim());
        departamentoService.guardar(departamento);
        logger.info("Departamento guardado: {}", nombre);
        return "redirect:/departamentos?exito=true";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("departamentos", departamentoService.listarTodos());
        model.addAttribute("editar", departamentoService.buscarPorId(id).orElseThrow());
        return "HU16-18/listarDepartamentos";
    }

    @GetMapping("/inactivar/{id}")
    public String inactivar(@PathVariable Long id) {
        departamentoService.inactivar(id);
        return "redirect:/departamentos?exito=true";
    }
}