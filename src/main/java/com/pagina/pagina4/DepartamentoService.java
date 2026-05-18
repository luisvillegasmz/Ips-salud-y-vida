package com.pagina.pagina4;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoService.class);
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> listarTodos() {
        return departamentoRepository.findAllByOrderByNombreAsc();
    }

    public void guardar(Departamento departamento) {
        departamentoRepository.save(departamento);
        logger.info("Departamento guardado: {}", departamento.getNombre());
    }

    public Optional<Departamento> buscarPorId(Long id) {
        return departamentoRepository.findById(id);
    }

    public void inactivar(Long id) {
        departamentoRepository.findById(id).ifPresent(d -> {
            d.setActivo(false);
            departamentoRepository.save(d);
            logger.info("Departamento inactivado: {}", d.getNombre());
        });
    }

    public boolean codigoDaneExiste(String codigo) {
        return departamentoRepository.existsByCodigoDane(codigo);
    }
}