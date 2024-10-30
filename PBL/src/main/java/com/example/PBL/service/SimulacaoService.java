package com.example.PBL.service;

import com.example.PBL.model.Simulacao;
import com.example.PBL.repositorio.SimulacaoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulacaoService {

    private final SimulacaoRepositorio simulacaoRepositorio;

    public SimulacaoService(SimulacaoRepositorio simulacaoRepositorio) {
        this.simulacaoRepositorio = simulacaoRepositorio;
    }

    public Simulacao salvarSimulacao(Simulacao simulacao) {
        return simulacaoRepositorio.save(simulacao);
    }

    // Novo método para buscar todas as simulações
    public List<Simulacao> buscarTodasSimulacoes() {
        return simulacaoRepositorio.findAll();
    }

    // Novo método para buscar uma simulação específica por ID
    public Simulacao buscarSimulacaoPorId(Long id) {
        return simulacaoRepositorio.findById(id).orElse(null);
    }
}