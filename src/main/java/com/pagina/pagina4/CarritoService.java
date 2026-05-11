package com.pagina.pagina4;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {
    private final ItemCarritoRepository itemCarritoRepository;
    private final PizzaService pizzaService;
    private final UsuarioRepository usuarioRepository;  // ← AGREGAR ESTO
    
    public CarritoService(ItemCarritoRepository itemCarritoRepository, 
                         PizzaService pizzaService,
                         UsuarioRepository usuarioRepository) {
        this.itemCarritoRepository = itemCarritoRepository;
        this.pizzaService = pizzaService;
        this.usuarioRepository = usuarioRepository;  // ← AGREGAR ESTO
    }
    
    public List<ItemCarrito> obtenerCarritoUsuario(Long usuarioId) {
        return itemCarritoRepository.findByUsuarioId(usuarioId);
    }
    
    @Transactional
    public void agregarAlCarrito(Long usuarioId, Long pizzaId, Integer cantidad) {
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
            .findByUsuarioIdAndPizzaId(usuarioId, pizzaId);
        
        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.setSubtotal(item.getPizza().getPrecio() * item.getCantidad());
            itemCarritoRepository.save(item);
        } else {
            Pizza pizza = pizzaService.obtenerPorId(pizzaId)
                .orElseThrow(() -> new RuntimeException("Pizza no encontrada"));
            
            // ← CAMBIAR ESTO:
            Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setUsuario(usuario);  // ← Ahora usa el usuario completo de la BD
            nuevoItem.setPizza(pizza);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setSubtotal(pizza.getPrecio() * cantidad);
            
            itemCarritoRepository.save(nuevoItem);
        }
    }
    
    @Transactional
    public void actualizarCantidad(Long usuarioId, Long pizzaId, Integer cantidad) {
        if (cantidad <= 0) {
            eliminarDelCarrito(usuarioId, pizzaId);
            return;
        }
        
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
            .findByUsuarioIdAndPizzaId(usuarioId, pizzaId);
        
        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(cantidad);
            item.setSubtotal(item.getPizza().getPrecio() * cantidad);
            itemCarritoRepository.save(item);
        }
    }
    
    @Transactional
    public void eliminarDelCarrito(Long usuarioId, Long pizzaId) {
        itemCarritoRepository.deleteByUsuarioIdAndPizzaId(usuarioId, pizzaId);
    }
    
    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        itemCarritoRepository.deleteByUsuarioId(usuarioId);
    }
    
    public Double calcularTotalCarrito(Long usuarioId) {
        List<ItemCarrito> items = obtenerCarritoUsuario(usuarioId);
        return items.stream()
            .mapToDouble(ItemCarrito::getSubtotal)
            .sum();
    }
    
    public Integer calcularCantidadTotal(Long usuarioId) {
        List<ItemCarrito> items = obtenerCarritoUsuario(usuarioId);
        return items.stream()
            .mapToInt(ItemCarrito::getCantidad)
            .sum();
    }
}