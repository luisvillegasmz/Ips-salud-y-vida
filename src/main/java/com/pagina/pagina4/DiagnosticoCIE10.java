package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "diagnosticos_cie10")
public class DiagnosticoCIE10 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String diagnostico;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;
}