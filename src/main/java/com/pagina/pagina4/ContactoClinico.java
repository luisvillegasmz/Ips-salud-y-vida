package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contactos_clinicos")
public class ContactoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId;

    // AC-1 HU-024: Área de texto amplia para describir el motivo
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    // AC-2 HU-024: Captura automática de fecha y hora del sistema
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    // AC-4 HU-024: Nombre del médico registrado automáticamente desde sesión
    @Column(nullable = false)
    private String nombreMedico;

    @Column
    private String codigoCIE10;
}
