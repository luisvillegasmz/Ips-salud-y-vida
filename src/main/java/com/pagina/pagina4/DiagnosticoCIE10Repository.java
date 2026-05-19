package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoCIE10Repository extends JpaRepository<DiagnosticoCIE10, Long> {

    boolean existsByCodigo(String codigo);

    Optional<DiagnosticoCIE10> findByCodigo(String codigo);

    @Query("SELECT d FROM DiagnosticoCIE10 d " +
           "WHERE LOWER(d.codigo) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "   OR LOWER(d.diagnostico) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<DiagnosticoCIE10> buscar(@Param("q") String q);

    List<DiagnosticoCIE10> findAllByOrderByCodigoAsc();
}
