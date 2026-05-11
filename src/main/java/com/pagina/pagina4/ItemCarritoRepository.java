package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(Long usuarioId);
    Optional<ItemCarrito> findByUsuarioIdAndPizzaId(Long usuarioId, Long pizzaId);
    void deleteByUsuarioId(Long usuarioId);
    void deleteByUsuarioIdAndPizzaId(Long usuarioId, Long pizzaId);
}