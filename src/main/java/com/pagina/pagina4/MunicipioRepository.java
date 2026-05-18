package com.pagina.pagina4;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    List<Municipio> findByDepartamentoId(Long departamentoId);
}