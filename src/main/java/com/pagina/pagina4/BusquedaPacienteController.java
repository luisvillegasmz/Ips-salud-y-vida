package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pacientes/buscar")
public class BusquedaPacienteController {

    private final PacienteRepository pacienteRepo;
    private final MotivoConsultaRepository motivoRepo;

    public BusquedaPacienteController(PacienteRepository pacienteRepo,
                                      MotivoConsultaRepository motivoRepo) {
        this.pacienteRepo = pacienteRepo;
        this.motivoRepo   = motivoRepo;
    }

    /* ── MOSTRAR BUSCADOR ────────────────────────────────────────── */
    @GetMapping
    public String mostrar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String filtro,   // recientes | prioritarios
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";

        List<Paciente> resultados = List.of();

        if (q != null && !q.trim().isEmpty()) {
            String term = q.trim().toLowerCase();
            resultados = pacienteRepo.findAll().stream()
                    .filter(p ->
                            p.getNombres().toLowerCase().contains(term) ||
                            p.getApellidos().toLowerCase().contains(term) ||
                            p.getNumeroDocumento().toLowerCase().contains(term))
                    .collect(Collectors.toList());
        } else if ("recientes".equals(filtro)) {
            // Últimos 10 pacientes registrados (mayor ID primero)
            resultados = pacienteRepo.findAll().stream()
                    .sorted(Comparator.comparingLong(Paciente::getId).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } else if ("prioritarios".equals(filtro)) {
            // Pacientes con síntomas severos en el motivo de consulta más reciente
            resultados = pacienteRepo.findAll().stream()
                    .filter(p -> {
                        var motivos = motivoRepo.findByPacienteIdOrderByFechaRegistroDesc(p.getId());
                        if (motivos.isEmpty()) return false;
                        MotivoConsulta ultimo = motivos.get(0);
                        return esSevero(ultimo.getFiebre())   ||
                               esSevero(ultimo.getTos())      ||
                               esSevero(ultimo.getDolorAbdominal()) ||
                               esSevero(ultimo.getNauseas())  ||
                               esSevero(ultimo.getMareo())    ||
                               esSevero(ultimo.getFatiga());
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("resultados", resultados);
        model.addAttribute("q", q);
        model.addAttribute("filtro", filtro);
        return "HU027/busquedaPacientes";
    }

    private boolean esSevero(String sintoma) {
        return "Severo".equalsIgnoreCase(sintoma);
    }
}