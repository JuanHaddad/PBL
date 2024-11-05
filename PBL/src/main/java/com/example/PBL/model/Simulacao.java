package com.example.PBL.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "simulacoes")
@Getter
@Setter
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_simulacao;

    private double frequencia;
    private double comprimentoOnda;
    private double duracao;
    private double erroMaximo;

    // Construtor padrão e construtor com parâmetros, se desejar
}
