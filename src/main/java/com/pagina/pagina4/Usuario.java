package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String apellido;

    @Column(unique = true, nullable = false)
    private String username; // AC-1: campo username separado del email

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String direccion;

    private String celular;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true; // AC-4: estado del usuario
}