package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente registrarPaciente(Paciente paciente) {

        if (paciente.getNumeroDocumento() == null ||
            paciente.getNumeroDocumento().trim().isEmpty()) {
            throw new RuntimeException("El número de documento no puede estar vacío.");
        }

        if (paciente.getNumeroDocumento().matches("\\d+")) {
            long numero = Long.parseLong(paciente.getNumeroDocumento());
            if (numero <= 0) {
                throw new RuntimeException("El número de documento no puede ser negativo o cero.");
            }
        }

        if (pacienteRepository.existsByNumeroDocumento(paciente.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un paciente registrado con ese número de documento.");
        }

        return pacienteRepository.save(paciente);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado."));
    }
}
