package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
        
    }
    
    public List<Pizza> listarTodas() {
        return pizzaRepository.findAll();
    }
    
    public Optional<Pizza> obtenerPorId(Long id) {
        return pizzaRepository.findById(id);
    }
    
    public Map<String, List<Pizza>> listarPizzasAgrupadas() {
        List<Pizza> todasPizzas = pizzaRepository.findAll();
        Map<String, List<Pizza>> agrupadas = new LinkedHashMap<>();
        
        for (Pizza pizza : todasPizzas) {
            String nombre = pizza.getNombre();
            if (!agrupadas.containsKey(nombre)) {
                agrupadas.put(nombre, new ArrayList<>());
            }
            agrupadas.get(nombre).add(pizza);
        }
        
        return agrupadas;
    }
}