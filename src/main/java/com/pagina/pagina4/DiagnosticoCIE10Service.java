package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiagnosticoCIE10Service {

    private final DiagnosticoCIE10Repository repo;

    public DiagnosticoCIE10Service(DiagnosticoCIE10Repository repo) {
        this.repo = repo;
    }

    public List<DiagnosticoCIE10> obtenerTodos() {
        return repo.findAllByOrderByCodigoAsc();
    }

    public List<DiagnosticoCIE10> buscar(String q) {
        if (q == null || q.trim().isEmpty()) return obtenerTodos();
        return repo.buscar(q.trim());
    }

    public DiagnosticoCIE10 obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con id: " + id));
    }

    public DiagnosticoCIE10 guardar(DiagnosticoCIE10 d) {
        String codigo = d.getCodigo().trim().toUpperCase();
        d.setCodigo(codigo);

        // Validar código único al crear
        if (d.getId() == null && repo.existsByCodigo(codigo)) {
            throw new RuntimeException("Ya existe un diagnóstico con el código " + codigo);
        }
        // Al editar: si cambió el código, verificar que no choque
        if (d.getId() != null) {
            repo.findByCodigo(codigo).ifPresent(existente -> {
                if (!existente.getId().equals(d.getId())) {
                    throw new RuntimeException("Ya existe otro diagnóstico con el código " + codigo);
                }
            });
        }
        return repo.save(d);
    }

    public void eliminar(Long id) {
        DiagnosticoCIE10 d = obtenerPorId(id);
        repo.delete(d);
    }

    public void cambiarEstado(Long id) {
        DiagnosticoCIE10 d = obtenerPorId(id);
        d.setActivo(!d.getActivo());
        repo.save(d);
    }
}