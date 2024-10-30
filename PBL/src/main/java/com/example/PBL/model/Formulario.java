package com.example.PBL.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Formulario")
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormulario;  // ID gerado automaticamente

    private Double frequencia;
    private Double comprimentoOnda;
    private Integer duracao;
    private Double erroMaximo;

    // Construtores
    public Formulario() {}

    public Formulario(Double frequencia, Double comprimentoOnda, Integer duracao, Double erroMaximo) {
        this.frequencia = frequencia;
        this.comprimentoOnda = comprimentoOnda;
        this.duracao = duracao;
        this.erroMaximo = erroMaximo;
    }

    // Getters e Setters
    public Long getIdFormulario() {
        return idFormulario;
    }

    public Double getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Double frequencia) {
        this.frequencia = frequencia;
    }

    public Double getComprimentoOnda() {
        return comprimentoOnda;
    }

    public void setComprimentoOnda(Double comprimentoOnda) {
        this.comprimentoOnda = comprimentoOnda;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Double getErroMaximo() {
        return erroMaximo;
    }

    public void setErroMaximo(Double erroMaximo) {
        this.erroMaximo = erroMaximo;
    }
}
