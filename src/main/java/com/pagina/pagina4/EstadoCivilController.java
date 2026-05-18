package com.pagina.pagina4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalogos/estado-civil")
public class EstadoCivilController {

    @Autowired
    private EstadoCivilService service;

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "estados",
                service.listarTodos()
        );

        model.addAttribute(
                "estadoCivil",
                new EstadoCivil()
        );

        return "estadoCivil/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model
    ) {

        EstadoCivil estadoCivil =
                service.buscarPorId(id).orElseThrow();

        model.addAttribute(
                "estados",
                service.listarTodos()
        );

        model.addAttribute(
                "estadoCivil",
                estadoCivil
        );

        return "estadoCivil/lista";
    }

    @PostMapping("/guardar")
    public String guardar(

            @ModelAttribute("estadoCivil")
            EstadoCivil estadoCivil,

            BindingResult result,
            Model model,
            RedirectAttributes ra
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "estados",
                    service.listarTodos()
            );

            return "estadoCivil/lista";
        }

        try {

            service.guardar(estadoCivil);

            ra.addFlashAttribute(
                    "exito",
                    "Estado civil guardado correctamente"
            );

        } catch (Exception e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/catalogos/estado-civil";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable Integer id
    ) {

        service.cambiarEstado(id);

        return "redirect:/catalogos/estado-civil";
    }
}