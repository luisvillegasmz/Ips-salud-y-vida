package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoClinicoService {

    private final ContactoClinicoRepository repo;

    public ContactoClinicoService(ContactoClinicoRepository repo) {
        this.repo = repo;
    }

    // AC-2: fecha/hora automática — AC-3: vinculado al pacienteId — AC-4: médico desde sesión
    public void registrar(Long pacienteId, String descripcion, String nombreMedico, String codigoCIE10) {
        ContactoClinico c = new ContactoClinico();
        c.setPacienteId(pacienteId);
        c.setDescripcion(descripcion.trim());
        c.setFechaHora(LocalDateTime.now());
        c.setNombreMedico(nombreMedico);
        c.setCodigoCIE10(codigoCIE10 != null && !codigoCIE10.isBlank() ? codigoCIE10.trim() : null);
        repo.save(c);
    }

    public Optional<ContactoClinico> ultimoPorPaciente(Long pacienteId) {
        List<ContactoClinico> lista = repo.findByPacienteIdOrderByFechaHoraDesc(pacienteId);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public List<ContactoClinico> porPaciente(Long pacienteId) {
        return repo.findByPacienteIdOrderByFechaHoraDesc(pacienteId);
    }
}
