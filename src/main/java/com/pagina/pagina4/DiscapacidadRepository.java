package com.pagina.pagina4;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscapacidadRepository extends JpaRepository<Discapacidad, Long> {
    List<Discapacidad> findByActivoTrue();
}