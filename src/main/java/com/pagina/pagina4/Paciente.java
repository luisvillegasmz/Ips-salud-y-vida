package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // AC-1: Tipo de documento (conectará con HU-011 cuando esté lista)
    @Column(nullable = false)
    private String tipoDocumento;

    // AC-2: Número de documento alfanumérico
    @Column(unique = true, nullable = false)
    private String numeroDocumento;

    // AC-4: Nombres y apellidos obligatorios
    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    
    // AC-5: Fecha y hora de nacimiento en columnas independientes
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "hora_nacimiento")
    private LocalTime horaNacimiento;

    @Column
    private String estadoCivil;

    @Column
    private String genero;

    @Column
    private String departamento;

    @Column
    private String municipio;
}