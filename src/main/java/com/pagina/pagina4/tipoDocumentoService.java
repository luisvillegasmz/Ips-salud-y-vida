package com.pagina.pagina4;

import com.ips.saludyvida.model.TipoDocumento;
import com.ips.saludyvida.repository.TipoDocumentoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDocumentoService {

    private static final Logger log =
            LoggerFactory.getLogger(TipoDocumentoService.class);

    @Autowired
    private TipoDocumentoRepository repository;

    public List<TipoDocumento> listarTodos() {
        return repository.findAllByOrderByNombreAsc();
    }

    public List<TipoDocumento> listarActivos() {
        return repository.findByActivoTrueOrderByNombreAsc();
    }

    public Optional<TipoDocumento> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public TipoDocumento guardar(TipoDocumento tipo) {

        tipo.setCodigo(tipo.getCodigo().toUpperCase().trim());

        if (tipo.getId() == null) {

            if (repository.existsByCodigo(tipo.getCodigo())) {
                throw new IllegalArgumentException(
                        "Ya existe el código: " + tipo.getCodigo()
                );
            }

        } else {

            if (repository.findByCodigoAndIdNot(
                    tipo.getCodigo(),
                    tipo.getId()
            ).isPresent()) {

                throw new IllegalArgumentException(
                        "Ya existe el código: " + tipo.getCodigo()
                );
            }
        }

        log.info("Guardando TipoDocumento: {}", tipo);

        return repository.save(tipo);
    }

    @Transactional
    public void cambiarEstado(Integer id) {

        TipoDocumento tipo = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No encontrado ID: " + id
                        )
                );

        tipo.setActivo(!tipo.getActivo());

        repository.save(tipo);

        log.info(
                "TipoDocumento ID={} -> activo={}",
                id,
                tipo.getActivo()
        );
    }
}
