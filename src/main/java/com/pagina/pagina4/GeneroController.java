package com.pagina.pagina4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalogos/genero")
public class GeneroController {

    @Autowired
    private GeneroService service;

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "generos",
                service.listarTodos()
        );

        model.addAttribute(
                "genero",
                new Genero()
        );

        return "genero/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model
    ) {

        Genero genero = service.buscarPorId(id).orElseThrow();

        model.addAttribute(
                "generos",
                service.listarTodos()
        );

        model.addAttribute(
                "genero",
                genero
        );

        return "genero/lista";
    }

    @PostMapping("/guardar")
    public String guardar(

            @Valid
            @ModelAttribute("genero")
            Genero genero,

            BindingResult result,
            Model model,
            RedirectAttributes ra
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "generos",
                    service.listarTodos()
            );

            return "genero/lista";
        }

        try {

            service.guardar(genero);

            ra.addFlashAttribute(
                    "exito",
                    "Género guardado correctamente"
            );

        } catch (Exception e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/catalogos/genero";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable Integer id
    ) {

        service.cambiarEstado(id);

        return "redirect:/catalogos/genero";
    }
}