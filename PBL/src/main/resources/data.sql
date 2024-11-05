-- Function para calcular a posição Y de um ponto da onda baseado em variáveis físicas
CREATE FUNCTION CalcularPosicaoY(
    @frequencia FLOAT,
    @comprimentoOnda FLOAT,
    @tempo FLOAT,
    @erroMax FLOAT
) RETURNS FLOAT
AS
BEGIN
    DECLARE @posY FLOAT;
    SET @posY = SIN(2 * PI() * (@frequencia * @tempo - (1 / @comprimentoOnda)));
    RETURN @posY;
END;
GO

-- Trigger para inserir 100 pontos na tabela GraficoSaida para cada nova entrada na tabela simulacoes
CREATE TRIGGER TriggerInserirGraficoSaida
ON simulacoes
AFTER INSERT
AS
BEGIN
    DECLARE @frequencia FLOAT;
    DECLARE @comprimentoOnda FLOAT;
    DECLARE @erroMax FLOAT;
    DECLARE @i INT = 0;
    DECLARE @tempo FLOAT = 0.0;

    -- Cursor para iterar sobre as novas linhas inseridas em simulacoes
    DECLARE simulacoes_cursor CURSOR FOR
    SELECT id_simulacao, frequencia, comprimento_onda, erro_maximo FROM inserted;

    DECLARE @idSimulacao BIGINT;

    OPEN simulacoes_cursor;
    FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Insere 100 pontos com o tempo variando de 0 a 10, em passos de 0.1
        WHILE @i < 100
        BEGIN
            SET @tempo = @i * 0.1;
            DECLARE @posY FLOAT = dbo.CalcularPosicaoY(@frequencia, @comprimentoOnda, @tempo, @erroMax);

            -- Define X fixo como 1 e insere o ponto com tempo e posição calculada
            INSERT INTO grafico_saida (id_simulacao, ponto_posicaox, ponto_posicaoy, ponto_tempo_exato)
            VALUES (@idSimulacao, 1, @posY, @tempo);

            SET @i = @i + 1;
        END

        SET @i = 0;
        FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;
    END

    CLOSE simulacoes_cursor;
    DEALLOCATE simulacoes_cursor;
END;
GO

