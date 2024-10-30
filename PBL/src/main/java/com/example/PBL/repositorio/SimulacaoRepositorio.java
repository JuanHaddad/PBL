package com.example.PBL.repositorio;

import com.example.PBL.model.Simulacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulacaoRepositorio extends JpaRepository<Simulacao, Long> {
    // Métodos adicionais podem ser definidos aqui, se necessário
}
