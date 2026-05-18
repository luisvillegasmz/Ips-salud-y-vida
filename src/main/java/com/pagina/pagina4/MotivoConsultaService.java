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

    public MotivoConsulta registrar(Long pacienteId,
                                    String descripcion,
                                    String fiebre, String tos,
                                    String dolorAbdominal, String nauseas,
                                    String mareo, String fatiga) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new RuntimeException("La descripción de síntomas es obligatoria.");
        }

        Paciente paciente = pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado."));

        MotivoConsulta m = new MotivoConsulta();
        m.setPaciente(paciente);
        m.setDescripcionSintomas(descripcion.trim());
        m.setFiebre(nullIfVacio(fiebre));
        m.setTos(nullIfVacio(tos));
        m.setDolorAbdominal(nullIfVacio(dolorAbdominal));
        m.setNauseas(nullIfVacio(nauseas));
        m.setMareo(nullIfVacio(mareo));
        m.setFatiga(nullIfVacio(fatiga));

        return repo.save(m);
    }

    public List<MotivoConsulta> porPaciente(Long pacienteId) {
        return repo.findByPacienteIdOrderByFechaRegistroDesc(pacienteId);
    }

    private String nullIfVacio(String v) {
        return (v == null || v.trim().isEmpty()) ? null : v.trim();
    }
}