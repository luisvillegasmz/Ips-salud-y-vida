package com.pagina.pagina4;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiscapacidadService {

    private static final Logger logger = LoggerFactory.getLogger(DiscapacidadService.class);
    private final DiscapacidadRepository discapacidadRepository;

    public DiscapacidadService(DiscapacidadRepository discapacidadRepository) {
        this.discapacidadRepository = discapacidadRepository;
    }

    public List<Discapacidad> listarTodas() {
        return discapacidadRepository.findAll();
    }

    public List<Discapacidad> listarActivas() {
        return discapacidadRepository.findByActivoTrue();
    }

    public void guardar(Discapacidad discapacidad) {
        discapacidadRepository.save(discapacidad);
        logger.info("Discapacidad guardada: {}", discapacidad.getNombre());
    }

    public Optional<Discapacidad> buscarPorId(Long id) {
        return discapacidadRepository.findById(id);
    }

    public void inactivar(Long id) {
        discapacidadRepository.findById(id).ifPresent(d -> {
            d.setActivo(false);
            discapacidadRepository.save(d);
            logger.info("Discapacidad inactivada: {}", d.getNombre());
        });
    }
}