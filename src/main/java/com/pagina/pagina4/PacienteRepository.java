package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);

    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.nombres)         LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(p.apellidos)       LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(p.numeroDocumento) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Paciente> buscarPorTermino(@Param("term") String term);
}