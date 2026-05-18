package com.pagina.pagina4;

package com.ips.saludyvida.repository;
import com.ips.saludyvida.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {

    boolean existsByCodigo(String codigo);

    Optional<TipoDocumento> findByCodigoAndIdNot(String codigo, Integer id);

    List<TipoDocumento> findByActivoTrueOrderByNombreAsc();

    List<TipoDocumento> findAllByOrderByNombreAsc();
}