package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping
    public String mostrar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String filtro,
            @RequestParam(defaultValue = "0") int page,   // ✅ AC-6: paginación
            Model model, HttpSession session) {

        if (session.getAttribute("usuarioActivo") == null) return "redirect:/inicioSesion";

        List<Paciente> resultados = List.of();

        if (q != null && !q.trim().isEmpty()) {
            // ✅ AC-5: usa @Query con LIKE en JPA, no stream en memoria
            resultados = pacienteRepo.buscarPorTermino(q.trim());
        } else if ("recientes".equals(filtro)) {
            resultados = pacienteRepo.findAll(PageRequest.of(page, 10))
                    .stream()
                    .sorted(Comparator.comparingLong(Paciente::getId).reversed())
                    .collect(Collectors.toList());
        } else if ("prioritarios".equals(filtro)) {
            resultados = pacienteRepo.findAll().stream()
                    .filter(p -> {
                        var motivos = motivoRepo.findByPacienteIdOrderByFechaRegistroDesc(p.getId());
                        if (motivos.isEmpty()) return false;
                        MotivoConsulta ultimo = motivos.get(0);
                        return esSevero(ultimo.getFiebre())        ||
                               esSevero(ultimo.getTos())           ||
                               esSevero(ultimo.getDolorAbdominal()) ||
                               esSevero(ultimo.getNauseas())       ||
                               esSevero(ultimo.getMareo())         ||
                               esSevero(ultimo.getFatiga());
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("resultados", resultados);
        model.addAttribute("q", q);
        model.addAttribute("filtro", filtro);
        model.addAttribute("page", page);
        return "HU24-30/BusquedaPacientes";
    }

    private boolean esSevero(String s) { return "Severo".equalsIgnoreCase(s); }
}