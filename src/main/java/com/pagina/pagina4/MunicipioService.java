package com.pagina.pagina4;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService {

    private static final Logger logger = LoggerFactory.getLogger(MunicipioService.class);
    private final MunicipioRepository municipioRepository;
    private final DepartamentoRepository departamentoRepository;

    public MunicipioService(MunicipioRepository municipioRepository,
                            DepartamentoRepository departamentoRepository) {
        this.municipioRepository = municipioRepository;
        this.departamentoRepository = departamentoRepository;
    }

    public List<Municipio> listarTodos() {
        return municipioRepository.findAll();
    }

    public void guardar(Municipio municipio) {
        municipioRepository.save(municipio);
        logger.info("Municipio guardado: {}", municipio.getNombre());
    }

    public Optional<Municipio> buscarPorId(Long id) {
        return municipioRepository.findById(id);
    }

    public Departamento buscarDepartamentoPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow();
    }
}