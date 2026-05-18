package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MotivoConsultaRepository extends JpaRepository<MotivoConsulta, Long> {

    List<MotivoConsulta> findByPacienteIdOrderByFechaRegistroDesc(Long pacienteId);
}
