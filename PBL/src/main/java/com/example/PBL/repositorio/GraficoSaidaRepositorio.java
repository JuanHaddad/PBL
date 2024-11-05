package com.example.PBL.repositorio;

import com.example.PBL.model.GraficoSaida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GraficoSaidaRepositorio extends JpaRepository<GraficoSaida, Long> {

    // Método para buscar um ponto de saída exato pelo ID da simulação e o tempo solicitado
    Optional<GraficoSaida> findByIdSimulacaoAndPontoTempoExato(Long idSimulacao, double pontoTempoExato);
}
