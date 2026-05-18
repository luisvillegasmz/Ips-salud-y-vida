package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {

    boolean existsByNombre(String nombre);

    Optional<Genero> findByNombreAndIdNot(String nombre, Integer id);

    List<Genero> findAllByOrderByNombreAsc();
}