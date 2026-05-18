package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoCivilRepository extends JpaRepository<EstadoCivil, Integer> {

    boolean existsByNombre(String nombre);

    Optional<EstadoCivil> findByNombreAndIdNot(String nombre, Integer id);

    List<EstadoCivil> findAllByOrderByNombreAsc();
}