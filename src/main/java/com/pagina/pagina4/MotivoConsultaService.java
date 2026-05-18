package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MotivoConsultaService {

    private final MotivoConsultaRepository repo;
    private final PacienteRepository pacienteRepo;

    public MotivoConsultaService(MotivoConsultaRepository repo,
                                 PacienteRepository pacienteRepo) {
        this.repo = repo;
        this.pacienteRepo = pacienteRepo;
    }

    public MotivoConsulta registrar(Long pacienteId, String descripcion,
                                    String nombreMedico,
                                    String fiebre, String tos,
                                    String dolorAbdominal, String nauseas,
                                    String mareo, String fatiga) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new RuntimeException("La descripción de síntomas es obligatoria.");
        }
        // ✅ AC-5 HU-024: longitud mínima
        if (descripcion.trim().length() < 10) {
            throw new RuntimeException("La descripción debe tener al menos 10 caracteres.");
        }

        Paciente paciente = pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado."));

        MotivoConsulta m = new MotivoConsulta();
        m.setPaciente(paciente);
        m.setDescripcionSintomas(descripcion.trim());
        m.setNombreMedico(nombreMedico != null ? nombreMedico : "Sin asignar"); // ✅ AC-4
        m.setFiebre(nvl(fiebre));
        m.setTos(nvl(tos));
        m.setDolorAbdominal(nvl(dolorAbdominal));
        m.setNauseas(nvl(nauseas));
        m.setMareo(nvl(mareo));
        m.setFatiga(nvl(fatiga));
        return repo.save(m);
    }

    public List<MotivoConsulta> porPaciente(Long pacienteId) {
        return repo.findByPacienteIdOrderByFechaRegistroDesc(pacienteId);
    }

    private String nvl(String v) {
        return (v == null || v.trim().isEmpty()) ? null : v.trim();
    }
}