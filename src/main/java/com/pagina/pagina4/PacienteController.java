package com.pagina.pagina4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {
    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);
    private final PacienteService pacienteService;
    private static final List<String> TIPOS_DOCUMENTO = List.of(
        "CC - Cédula de Ciudadanía",
        "TI - Tarjeta de Identidad",
        "CE - Cédula de Extranjería",
        "PA - Pasaporte",
        "RC - Registro Civil"
    );

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(
            @RequestParam(required = false) String exito,
            Model model) {
        model.addAttribute("tiposDocumento", TIPOS_DOCUMENTO);
        if (exito != null) {
            model.addAttribute("exito", "Paciente registrado exitosamente.");
        }
        return "HU01-03/registroPaciente";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            Model model) {
        if (nombres.trim().isEmpty() || apellidos.trim().isEmpty() ||
            numeroDocumento.trim().isEmpty() || tipoDocumento.trim().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            model.addAttribute("tiposDocumento", TIPOS_DOCUMENTO);
            return "HU01-03/registroPaciente"; 
        }
        try {
            Paciente nuevo = new Paciente();
            nuevo.setTipoDocumento(tipoDocumento);
            nuevo.setNumeroDocumento(numeroDocumento.trim());
            nuevo.setNombres(nombres.trim());
            nuevo.setApellidos(apellidos.trim());
            nuevo.setFechaNacimiento(fechaNacimiento);
            pacienteService.registrarPaciente(nuevo);
            logger.info("Paciente registrado: {} - {}", tipoDocumento, numeroDocumento);
            return "redirect:/pacientes/registro?exito=true";
        } catch (RuntimeException e) {
            logger.warn("Error al registrar paciente: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tiposDocumento", TIPOS_DOCUMENTO);
            return "HU01-03/registroPaciente";
        }
    }
}