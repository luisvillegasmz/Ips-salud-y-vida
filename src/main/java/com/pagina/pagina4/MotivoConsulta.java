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
@Table(name = "motivos_consulta")
public class MotivoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Paciente asociado (FK) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /** Texto libre de descripción de síntomas */
    @Column(nullable = false, length = 1000)
    private String descripcionSintomas;

    /* ── Síntomas con severidad ──────────────────────────────────── */

    /** Sin síntoma = null; valores: Leve, Moderado, Severo */
    private String fiebre;
    private String tos;
    private String dolorAbdominal;
    private String nauseas;
    private String mareo;
    private String fatiga;

    /** Fecha y hora de registro */
    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}