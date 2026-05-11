package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    List<Pizza> findByNombre(String nombre);
    List<Pizza> findByTamanio(String tamanio);
}