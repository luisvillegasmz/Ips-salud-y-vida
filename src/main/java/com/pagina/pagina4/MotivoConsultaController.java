package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consulta/motivo")
public class MotivoConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(MotivoConsultaController.class);
    private final MotivoConsultaService service;
    private final PacienteService pacienteService;
    private static final java.util.List<String> SEVERIDADES =
            java.util.List.of("Leve", "Moderado", "Severo");

    public MotivoConsultaController(MotivoConsultaService service,
                                    PacienteService pacienteService) {
        this.service = service;
        this.pacienteService = pacienteService;
    }

    @GetMapping("/{pacienteId}")
    public String mostrar(@PathVariable Long pacienteId,
                          @RequestParam(required = false) String exito,
                          Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";
        model.addAttribute("paciente", pacienteService.obtenerPorId(pacienteId));
        model.addAttribute("severidades", SEVERIDADES);
        model.addAttribute("historial", service.porPaciente(pacienteId));
        if (exito != null) model.addAttribute("exito", "Consulta registrada exitosamente.");
        return "HU24-30/MotivoConsulta";
    }

    @PostMapping("/{pacienteId}")
    public String procesar(
            @PathVariable Long pacienteId,
            @RequestParam String descripcionSintomas,
            @RequestParam(required = false) String fiebre,
            @RequestParam(required = false) String tos,
            @RequestParam(required = false) String dolorAbdominal,
            @RequestParam(required = false) String nauseas,
            @RequestParam(required = false) String mareo,
            @RequestParam(required = false) String fatiga,
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";

        // ✅ AC-4 HU-024: captura el médico desde la sesión activa
        Usuario medico = (Usuario) session.getAttribute("usuarioActivo");
        String nombreMedico = medico.getNombre() + " " + medico.getApellido();

        try {
            service.registrar(pacienteId, descripcionSintomas, nombreMedico,
                    fiebre, tos, dolorAbdominal, nauseas, mareo, fatiga);
            logger.info("Consulta registrada - pacienteId={}, médico={}", pacienteId, nombreMedico);
            return "redirect:/consulta/motivo/" + pacienteId + "?exito=true";
        } catch (RuntimeException e) {
            logger.warn("Error al registrar consulta: {}", e.getMessage());
            model.addAttribute("paciente", pacienteService.obtenerPorId(pacienteId));
            model.addAttribute("severidades", SEVERIDADES);
            model.addAttribute("historial", service.porPaciente(pacienteId));
            model.addAttribute("error", e.getMessage());
            return "HU24-30/MotivoConsulta";
        }
    }
}