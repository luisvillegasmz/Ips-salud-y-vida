package com.pagina.pagina4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository repository;

    public List<Genero> listarTodos() {
        return repository.findAllByOrderByNombreAsc();
    }

    public Optional<Genero> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Genero guardar(Genero genero) {

        if (genero.getId() == null) {

            if (repository.existsByNombre(genero.getNombre())) {
                throw new IllegalArgumentException("El género ya existe");
            }

        } else {

            if (repository.findByNombreAndIdNot(
                    genero.getNombre(),
                    genero.getId()
            ).isPresent()) {

                throw new IllegalArgumentException("El género ya existe");
            }
        }

        return repository.save(genero);
    }

    @Transactional
    public void cambiarEstado(Integer id) {

        Genero genero = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No encontrado")
                );

        genero.setActivo(!genero.getActivo());

        repository.save(genero);
    }
}