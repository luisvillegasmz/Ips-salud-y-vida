package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // AC-6: Verificar si ya existe por número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);

    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
}