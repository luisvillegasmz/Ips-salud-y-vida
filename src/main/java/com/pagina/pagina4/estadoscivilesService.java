package com.pagina.pagina4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoCivilService {

    @Autowired
    private EstadoCivilRepository repository;

    public List<EstadoCivil> listarTodos() {
        return repository.findAllByOrderByNombreAsc();
    }

    public Optional<EstadoCivil> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public EstadoCivil guardar(EstadoCivil estadoCivil) {

        if (estadoCivil.getId() == null) {

            if (repository.existsByNombre(estadoCivil.getNombre())) {
                throw new IllegalArgumentException("El estado civil ya existe");
            }

        } else {

            if (repository.findByNombreAndIdNot(
                    estadoCivil.getNombre(),
                    estadoCivil.getId()
            ).isPresent()) {

                throw new IllegalArgumentException("El estado civil ya existe");
            }
        }

        return repository.save(estadoCivil);
    }

    @Transactional
    public void cambiarEstado(Integer id) {

        EstadoCivil estadoCivil = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No encontrado")
                );

        estadoCivil.setActivo(!estadoCivil.getActivo());

        repository.save(estadoCivil);
    }
}