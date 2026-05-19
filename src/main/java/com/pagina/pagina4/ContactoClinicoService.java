package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoClinicoService {

    private final MotivoConsultaRepository motivoRepo;
    private final PacienteRepository pacienteRepo;

    public ContactoClinicoService(MotivoConsultaRepository motivoRepo,
                                  PacienteRepository pacienteRepo) {
        this.motivoRepo  = motivoRepo;
        this.pacienteRepo = pacienteRepo;
    }

    // Guarda el motivo de consulta rápido desde el formulario de registro de paciente
    public void registrar(Long pacienteId, String descripcion,
                          String nombreMedico, String codigoCIE10) {
        Paciente paciente = pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado."));
        MotivoConsulta m = new MotivoConsulta();
        m.setPaciente(paciente);
        m.setDescripcionSintomas(descripcion.trim());
        m.setNombreMedico(nombreMedico != null ? nombreMedico : "Sistema");
        m.setCodigoCIE10(codigoCIE10 != null && !codigoCIE10.isBlank()
                ? codigoCIE10.trim() : null);
        motivoRepo.save(m);
    }

    public Optional<MotivoConsulta> ultimoPorPaciente(Long pacienteId) {
        List<MotivoConsulta> lista =
                motivoRepo.findByPacienteIdOrderByFechaRegistroDesc(pacienteId);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public List<MotivoConsulta> porPaciente(Long pacienteId) {
        return motivoRepo.findByPacienteIdOrderByFechaRegistroDesc(pacienteId);
    }
}
