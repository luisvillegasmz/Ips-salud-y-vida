package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactoClinicoRepository extends JpaRepository<ContactoClinico, Long> {
    List<ContactoClinico> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);
}
