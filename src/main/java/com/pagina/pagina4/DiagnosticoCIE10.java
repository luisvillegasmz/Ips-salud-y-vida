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

    /** Ej: J00, E11, I10 */
    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    /** Nombre del diagnóstico */
    @Column(nullable = false, length = 200)
    private String diagnostico;

    /** Categoría / sistema: Respiratorio, Endocrino, Cardiovascular… */
    @Column(nullable = false, length = 100)
    private String categoria;

    /** true = Activo, false = Inactivo */
    @Column(nullable = false)
    private Boolean activo = true;
}