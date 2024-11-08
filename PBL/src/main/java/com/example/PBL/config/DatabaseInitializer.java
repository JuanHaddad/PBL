package com.example.PBL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Aguarda alguns segundos para garantir que o contexto do Spring Boot foi inicializado
            Thread.sleep(10000);

            // Criação da função CalcularPosicaoY se não existir
            if (!functionExists("CalcularPosicaoY")) {
                jdbcTemplate.execute("CREATE FUNCTION CalcularPosicaoY(\n" +
                        "    @frequencia FLOAT,\n" +
                        "    @comprimentoOnda FLOAT,\n" +
                        "    @tempo FLOAT,\n" +
                        "    @erroMax FLOAT\n" +
                        ") RETURNS FLOAT\n" +
                        "AS\n" +
                        "BEGIN\n" +
                        "    DECLARE @posY FLOAT;\n" +
                        "    SET @posY = SIN(2 * PI() * (@frequencia * @tempo - (1 / @comprimentoOnda)));\n" +
                        "    RETURN @posY;\n" +
                        "END;");
            }

            // Criação da stored procedure InserirGraficoSaida se não existir
            if (!procedureExists("InserirGraficoSaida")) {
                jdbcTemplate.execute("CREATE PROCEDURE InserirGraficoSaida\n" +
                        "    @idSimulacao BIGINT,\n" +
                        "    @frequencia FLOAT,\n" +
                        "    @comprimentoOnda FLOAT,\n" +
                        "    @erroMax FLOAT\n" +
                        "AS\n" +
                        "BEGIN\n" +
                        "    DECLARE @i INT = 0;\n" +
                        "    DECLARE @tempo FLOAT;\n" +
                        "\n" +
                        "    WHILE @i < 100\n" +
                        "    BEGIN\n" +
                        "        SET @tempo = @i * 0.1;\n" +
                        "        DECLARE @posY FLOAT = dbo.CalcularPosicaoY(@frequencia, @comprimentoOnda, @tempo, @erroMax);\n" +
                        "        INSERT INTO grafico_saida (id_simulacao, ponto_posicaox, ponto_posicaoy, ponto_tempo_exato)\n" +
                        "        VALUES (@idSimulacao, 1, @posY, @tempo);\n" +
                        "        SET @i = @i + 1;\n" +
                        "    END\n" +
                        "END;");
            }

            // Criação do trigger TriggerInserirGraficoSaida se não existir
            if (!triggerExists("TriggerInserirGraficoSaida")) {
                jdbcTemplate.execute("CREATE TRIGGER TriggerInserirGraficoSaida\n" +
                        "ON simulacoes\n" +
                        "AFTER INSERT\n" +
                        "AS\n" +
                        "BEGIN\n" +
                        "    DECLARE @idSimulacao BIGINT;\n" +
                        "    DECLARE @frequencia FLOAT;\n" +
                        "    DECLARE @comprimentoOnda FLOAT;\n" +
                        "    DECLARE @erroMax FLOAT;\n" +
                        "\n" +
                        "    DECLARE simulacoes_cursor CURSOR FOR\n" +
                        "    SELECT id_simulacao, frequencia, comprimento_onda, erro_maximo FROM inserted;\n" +
                        "\n" +
                        "    OPEN simulacoes_cursor;\n" +
                        "    FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;\n" +
                        "\n" +
                        "    WHILE @@FETCH_STATUS = 0\n" +
                        "    BEGIN\n" +
                        "        EXEC InserirGraficoSaida @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;\n" +
                        "        FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;\n" +
                        "    END\n" +
                        "\n" +
                        "    CLOSE simulacoes_cursor;\n" +
                        "    DEALLOCATE simulacoes_cursor;\n" +
                        "END;");
            }

            // Criação da stored procedure ExcluirSimulacaoRelacional se não existir
            if (!procedureExists("ExcluirSimulacaoRelacional")) {
                jdbcTemplate.execute("CREATE PROCEDURE ExcluirSimulacaoRelacional\n" +
                        "    @idSimulacao BIGINT\n" +
                        "AS\n" +
                        "BEGIN\n" +
                        "    DELETE FROM grafico_saida WHERE id_simulacao = @idSimulacao;\n" +
                        "END;");
            }

            // Criação do trigger TriggerExcluirSimulacaoRelacional se não existir
            if (!triggerExists("TriggerExcluirSimulacaoRelacional")) {
                jdbcTemplate.execute("CREATE TRIGGER TriggerExcluirSimulacaoRelacional\n" +
                        "ON simulacoes\n" +
                        "AFTER DELETE\n" +
                        "AS\n" +
                        "BEGIN\n" +
                        "    DECLARE @idSimulacao BIGINT;\n" +
                        "\n" +
                        "    DECLARE deleted_cursor CURSOR FOR\n" +
                        "    SELECT id_simulacao FROM deleted;\n" +
                        "\n" +
                        "    OPEN deleted_cursor;\n" +
                        "    FETCH NEXT FROM deleted_cursor INTO @idSimulacao;\n" +
                        "\n" +
                        "    WHILE @@FETCH_STATUS = 0\n" +
                        "    BEGIN\n" +
                        "        EXEC ExcluirSimulacaoRelacional @idSimulacao;\n" +
                        "        FETCH NEXT FROM deleted_cursor INTO @idSimulacao;\n" +
                        "    END\n" +
                        "\n" +
                        "    CLOSE deleted_cursor;\n" +
                        "    DEALLOCATE deleted_cursor;\n" +
                        "END;");
            }
        };
    }

    private boolean functionExists(String functionName) {
        String sql = "SELECT COUNT(*) FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo." + functionName + "') AND type IN (N'FN', N'IF', N'TF', N'FS', N'FT')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    private boolean procedureExists(String procedureName) {
        String sql = "SELECT COUNT(*) FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo." + procedureName + "') AND type = 'P'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    private boolean triggerExists(String triggerName) {
        String sql = "SELECT COUNT(*) FROM sys.triggers WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{triggerName}, Integer.class);
        return count != null && count > 0;
    }
}
