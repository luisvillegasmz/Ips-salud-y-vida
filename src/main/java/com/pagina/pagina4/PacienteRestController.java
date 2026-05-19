package com.pagina.pagina4;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteRestController {

    private final PacienteService pacienteService;

    public PacienteRestController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Paciente paciente) {
        try {
            Paciente creado = pacienteService.registrarPaciente(paciente);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }
}