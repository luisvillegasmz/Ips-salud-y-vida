package com.pagina.pagina4;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "contactos_clinicos")
public class MotivoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    private String nombreMedico;

    @Column(nullable = false, length = 1000)
    private String descripcionSintomas;

    private String fiebre;
    private String tos;
    private String dolorAbdominal;
    private String nauseas;
    private String mareo;
    private String fatiga;

    @Column(name = "codigocie10")
    private String codigoCIE10;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}