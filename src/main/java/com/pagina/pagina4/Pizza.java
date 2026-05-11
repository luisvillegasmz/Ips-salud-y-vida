package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String tamanio; 

    @Column(nullable = false)
    private Double precio;
    
    private String imagen;
    
    @Column(length = 500)
    private String descripcion;
    
    public Pizza(String nombre, String tamanio, Double precio, String imagen, String descripcion) {
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }
}