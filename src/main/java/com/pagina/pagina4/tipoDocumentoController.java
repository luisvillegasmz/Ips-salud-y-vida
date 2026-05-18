package com.pagina.pagina4;

import com.pagina.pagina4.TipoDocumento;
import com.pagina.pagina4.TipoDocumentoService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/catalogos/tipo-documento")
public class TipoDocumentoController {

    private static final Logger log =
            LoggerFactory.getLogger(TipoDocumentoController.class);

    @Autowired
    private TipoDocumentoService service;

    @GetMapping
    public String listar(Model model) {

        log.info("GET /catalogos/tipo-documento");

        model.addAttribute("tipos", service.listarTodos());

        model.addAttribute(
                "tipoDocumento",
                new TipoDocumento()
        );

        return "tipoDocumento/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes ra
    ) {

        return service.buscarPorId(id)
                .map(tipo -> {

                    model.addAttribute(
                            "tipos",
                            service.listarTodos()
                    );

                    model.addAttribute(
                            "tipoDocumento",
                            tipo
                    );

                    return "tipoDocumento/lista";
                })

                .orElseGet(() -> {

                    ra.addFlashAttribute(
                            "error",
                            "No encontrado"
                    );

                    return "redirect:/catalogos/tipo-documento";
                });
    }

    @PostMapping("/guardar")
    public String guardar(

            @Valid
            @ModelAttribute("tipoDocumento")
            TipoDocumento tipo,

            BindingResult result,
            Model model,
            RedirectAttributes ra
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "tipos",
                    service.listarTodos()
            );

            return "tipoDocumento/lista";
        }

        try {

            service.guardar(tipo);

            ra.addFlashAttribute(
                    "exito",
                    "Tipo de documento guardado correctamente"
            );

        } catch (IllegalArgumentException e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/catalogos/tipo-documento";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(
            @PathVariable Integer id,
            RedirectAttributes ra
    ) {

        try {

            service.cambiarEstado(id);

            ra.addFlashAttribute(
                    "exito",
                    "Estado actualizado"
            );

        } catch (IllegalArgumentException e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/catalogos/tipo-documento";
    }

    // API POSTMAN

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<TipoDocumento>> apiListar() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> apiGuardar(
            @RequestBody TipoDocumento tipo
    ) {

        try {

            return ResponseEntity.ok(
                    service.guardar(tipo)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}