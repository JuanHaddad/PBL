package com.example.PBL.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grafico_saida")
@Getter
@Setter
public class GraficoSaida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ponto_saida;

    private Long idSimulacao;
    private double pontoPosicaoX;
    private double pontoPosicaoY;
    private double pontoTempoExato;

    // Construtor padrão e outros métodos, se necessário
}
