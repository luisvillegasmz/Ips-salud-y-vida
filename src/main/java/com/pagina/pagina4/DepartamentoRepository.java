package com.pagina.pagina4;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    List<Departamento> findAllByOrderByNombreAsc();
    boolean existsByCodigoDane(String codigoDane);
}