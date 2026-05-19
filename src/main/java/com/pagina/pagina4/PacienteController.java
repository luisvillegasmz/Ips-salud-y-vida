package com.pagina.pagina4;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    private final PacienteService pacienteService;
    private final TipoDocumentoService tipoDocumentoService;
    private final EstadoCivilService estadoCivilService;
    private final GeneroService generoService;
    private final DepartamentoService departamentoService;
    private final MunicipioService municipioService;
    private final DiagnosticoCIE10Service cie10Service;
    private final ContactoClinicoService contactoClinicoService;

    public PacienteController(PacienteService pacienteService,
                               TipoDocumentoService tipoDocumentoService,
                               EstadoCivilService estadoCivilService,
                               GeneroService generoService,
                               DepartamentoService departamentoService,
                               MunicipioService municipioService,
                               DiagnosticoCIE10Service cie10Service,
                               ContactoClinicoService contactoClinicoService) {
        this.pacienteService = pacienteService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.estadoCivilService = estadoCivilService;
        this.generoService = generoService;
        this.departamentoService = departamentoService;
        this.municipioService = municipioService;
        this.cie10Service = cie10Service;
        this.contactoClinicoService = contactoClinicoService;
    }

    private void poblarCatalogos(Model model) {
        model.addAttribute("tiposDocumento",   tipoDocumentoService.listarActivos());
        model.addAttribute("estadosCiviles",   estadoCivilService.listarTodos());
        model.addAttribute("generos",          generoService.listarTodos());
        model.addAttribute("departamentos",    departamentoService.listarTodos());
        model.addAttribute("municipios",       municipioService.listarTodos());
        model.addAttribute("diagnosticoscie10",
                cie10Service.obtenerTodos().stream()
                        .filter(d -> Boolean.TRUE.equals(d.getActivo()))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/registro")
    public String mostrarFormulario(@RequestParam(required = false) String exito, Model model) {
        poblarCatalogos(model);
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaNacimiento,
            @RequestParam(required = false) String estadoCivil,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String motivoConsulta,
            @RequestParam(required = false) String codigoCIE10,
            Model model, HttpSession session) {

        if (nombres.trim().isEmpty() || apellidos.trim().isEmpty() ||
            numeroDocumento.trim().isEmpty() || tipoDocumento.trim().isEmpty()) {
            model.addAttribute("error", "Todos los campos obligatorios deben completarse.");
            poblarCatalogos(model);
            return "HU01-03/registroPaciente";
        }

        try {
            Paciente nuevo = new Paciente();
            nuevo.setTipoDocumento(tipoDocumento);
            nuevo.setNumeroDocumento(numeroDocumento.trim());
            nuevo.setNombres(nombres.trim());
            nuevo.setApellidos(apellidos.trim());
            nuevo.setFechaNacimiento(fechaNacimiento);
            nuevo.setHoraNacimiento(horaNacimiento);
            nuevo.setEstadoCivil(estadoCivil);
            nuevo.setGenero(genero);
            nuevo.setDepartamento(departamento);
            nuevo.setMunicipio(municipio);

            Paciente guardado = pacienteService.registrarPaciente(nuevo);
            logger.info("Paciente registrado: {} - {}", tipoDocumento, numeroDocumento);

            if (motivoConsulta != null && motivoConsulta.trim().length() >= 10) {
                Usuario medico = (Usuario) session.getAttribute("usuarioActivo");
                String nombreMedico = medico != null
                        ? medico.getNombre() + " " + medico.getApellido()
                        : "Médico";
                contactoClinicoService.registrar(
                        guardado.getId(), motivoConsulta, nombreMedico, codigoCIE10);
                logger.info("Contacto clínico registrado para paciente ID {}", guardado.getId());
            }

            return "redirect:/pacientes/registro?exito=true";

        } catch (RuntimeException e) {
            logger.warn("Error al registrar paciente: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            poblarCatalogos(model);
            return "HU01-03/registroPaciente";
        }
    }
}
