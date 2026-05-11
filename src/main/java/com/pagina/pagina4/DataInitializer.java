package com.pagina.pagina4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PizzaRepository pizzaRepository;

    public DataInitializer(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (pizzaRepository.count() == 0) {
            insertarPizzas();
        }
    }

    private void insertarPizzas() {
        // Hawaiana
        pizzaRepository.save(new Pizza("Hawaiana", "Pequeña", 18000.0, "/img/Hawaiana.jpg", "Jamón y piña"));
        pizzaRepository.save(new Pizza("Hawaiana", "Mediana", 26000.0, "/img/Hawaiana.jpg", "Jamón y piña"));
        pizzaRepository.save(new Pizza("Hawaiana", "Grande", 32000.0, "/img/Hawaiana.jpg", "Jamón y piña"));
        
        // Mexicana
        pizzaRepository.save(new Pizza("Mexicana", "Pequeña", 20000.0, "/img/Mexicana.jpg", "Jalapeños y carne"));
        pizzaRepository.save(new Pizza("Mexicana", "Mediana", 28000.0, "/img/Mexicana.jpg", "Jalapeños y carne"));
        pizzaRepository.save(new Pizza("Mexicana", "Grande", 35000.0, "/img/Mexicana.jpg", "Jalapeños y carne"));
        
        // Napolitana
        pizzaRepository.save(new Pizza("Napolitana", "Pequeña", 17000.0, "/img/napolitana.jpg", "Tomate y albahaca"));
        pizzaRepository.save(new Pizza("Napolitana", "Mediana", 25000.0, "/img/napolitana.jpg", "Tomate y albahaca"));
        pizzaRepository.save(new Pizza("Napolitana", "Grande", 31000.0, "/img/napolitana.jpg", "Tomate y albahaca"));
        
        // Vegetariana
        pizzaRepository.save(new Pizza("Vegetariana", "Pequeña", 19000.0, "/img/vegetariana.jpg", "Vegetales frescos"));
        pizzaRepository.save(new Pizza("Vegetariana", "Mediana", 27000.0, "/img/vegetariana.jpg", "Vegetales frescos"));
        pizzaRepository.save(new Pizza("Vegetariana", "Grande", 34000.0, "/img/vegetariana.jpg", "Vegetales frescos"));
        
        // Cuatro Quesos
        pizzaRepository.save(new Pizza("Cuatro Quesos", "Pequeña", 21000.0, "/img/cuatro-quesos.jpg", "Mezcla de quesos"));
        pizzaRepository.save(new Pizza("Cuatro Quesos", "Mediana", 30000.0, "/img/cuatro-quesos.jpg", "Mezcla de quesos"));
        pizzaRepository.save(new Pizza("Cuatro Quesos", "Grande", 37000.0, "/img/cuatro-quesos.jpg", "Mezcla de quesos"));
        
        // Pollo y Champiñón
        pizzaRepository.save(new Pizza("Pollo y Champiñón", "Pequeña", 20000.0, "/img/pollo-champinon.jpg", "Pollo y champiñones"));
        pizzaRepository.save(new Pizza("Pollo y Champiñón", "Mediana", 29000.0, "/img/pollo-champinon.jpg", "Pollo y champiñones"));
        pizzaRepository.save(new Pizza("Pollo y Champiñón", "Grande", 36000.0, "/img/pollo-champinon.jpg", "Pollo y champiñones"));
        
        // Jamón y Queso
        pizzaRepository.save(new Pizza("Jamón y Queso", "Pequeña", 18000.0, "/img/jamon-queso.jpg", "Clásica"));
        pizzaRepository.save(new Pizza("Jamón y Queso", "Mediana", 26000.0, "/img/jamon-queso.jpg", "Clásica"));
        pizzaRepository.save(new Pizza("Jamón y Queso", "Grande", 32000.0, "/img/jamon-queso.jpg", "Clásica"));
        
        // Pepperoni
        pizzaRepository.save(new Pizza("Pepperoni", "Pequeña", 19000.0, "/img/pepperoni.jpg", "Abundante pepperoni"));
        pizzaRepository.save(new Pizza("Pepperoni", "Mediana", 27000.0, "/img/pepperoni.jpg", "Abundante pepperoni"));
        pizzaRepository.save(new Pizza("Pepperoni", "Grande", 34000.0, "/img/pepperoni.jpg", "Abundante pepperoni"));
        
        // Mar y Tierra
        pizzaRepository.save(new Pizza("Mar y Tierra", "Pequeña", 23000.0, "/img/mar-tierra.jpg", "Mariscos y carnes"));
        pizzaRepository.save(new Pizza("Mar y Tierra", "Mediana", 32000.0, "/img/mar-tierra.jpg", "Mariscos y carnes"));
        pizzaRepository.save(new Pizza("Mar y Tierra", "Grande", 40000.0, "/img/mar-tierra.jpg", "Mariscos y carnes"));
        
        // BBQ Especial
        pizzaRepository.save(new Pizza("BBQ Especial", "Pequeña", 22000.0, "/img/bbq-especial.jpg", "Salsa BBQ"));
        pizzaRepository.save(new Pizza("BBQ Especial", "Mediana", 31000.0, "/img/bbq-especial.jpg", "Salsa BBQ"));
        pizzaRepository.save(new Pizza("BBQ Especial", "Grande", 39000.0, "/img/bbq-especial.jpg", "Salsa BBQ"));
        
        System.out.println("30 pizzas insertadas en la BD");
    }
}