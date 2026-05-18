package com.pagina.pagina4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);
    private final PacienteService pacienteService;
    private final TipoDocumentoService tipoDocumentoService;
    private final EstadoCivilService estadoCivilService;

    public PacienteController(PacienteService pacienteService,
                               TipoDocumentoService tipoDocumentoService,
                               EstadoCivilService estadoCivilService) {
        this.pacienteService = pacienteService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.estadoCivilService = estadoCivilService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(@RequestParam(required = false) String exito, Model model) {
        model.addAttribute("tiposDocumento", tipoDocumentoService.listarActivos());
        model.addAttribute("estadosCiviles", estadoCivilService.listarTodos());
        if (exito != null) model.addAttribute("exito", "Paciente registrado exitosamente.");
        return "HU01-03/registroPaciente";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam(required = false) String estadoCivil,
            Model model) {

        if (nombres.trim().isEmpty() || apellidos.trim().isEmpty() ||
            numeroDocumento.trim().isEmpty() || tipoDocumento.trim().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            model.addAttribute("tiposDocumento", tipoDocumentoService.listarActivos());
            model.addAttribute("estadosCiviles", estadoCivilService.listarTodos());
            return "HU01-03/registroPaciente";
        }
        try {
            Paciente nuevo = new Paciente();
            nuevo.setTipoDocumento(tipoDocumento);
            nuevo.setNumeroDocumento(numeroDocumento.trim());
            nuevo.setNombres(nombres.trim());
            nuevo.setApellidos(apellidos.trim());
            nuevo.setFechaNacimiento(fechaNacimiento);
            nuevo.setEstadoCivil(estadoCivil);
            pacienteService.registrarPaciente(nuevo);
            logger.info("Paciente registrado: {} - {}", tipoDocumento, numeroDocumento);
            return "redirect:/pacientes/registro?exito=true";
        } catch (RuntimeException e) {
            logger.warn("Error al registrar paciente: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiposDocumento", tipoDocumentoService.listarActivos());
            model.addAttribute("estadosCiviles", estadoCivilService.listarTodos());
            return "HU01-03/registroPaciente";
        }
    }
}