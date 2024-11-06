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



CREATE PROCEDURE InserirGraficoSaida
    @idSimulacao BIGINT,
    @frequencia FLOAT,
    @comprimentoOnda FLOAT,
    @erroMax FLOAT
AS
BEGIN
    DECLARE @i INT = 0;
    DECLARE @tempo FLOAT;

    WHILE @i < 100
    BEGIN
        SET @tempo = @i * 0.1;  -- Tempo variando de 0 a 10, em passos de 0.1
        DECLARE @posY FLOAT = dbo.CalcularPosicaoY(@frequencia, @comprimentoOnda, @tempo, @erroMax);

        -- Insere o ponto calculado na tabela grafico_saida
        INSERT INTO grafico_saida (id_simulacao, ponto_posicaox, ponto_posicaoy, ponto_tempo_exato)
        VALUES (@idSimulacao, 1, @posY, @tempo);

        SET @i = @i + 1;
    END
END;
GO





CREATE TRIGGER TriggerInserirGraficoSaida
ON simulacoes
AFTER INSERT
AS
BEGIN
    DECLARE @idSimulacao BIGINT;
    DECLARE @frequencia FLOAT;
    DECLARE @comprimentoOnda FLOAT;
    DECLARE @erroMax FLOAT;

    -- Cursor para iterar sobre as novas linhas inseridas em simulacoes
    DECLARE simulacoes_cursor CURSOR FOR
    SELECT id_simulacao, frequencia, comprimento_onda, erro_maximo FROM inserted;

    OPEN simulacoes_cursor;
    FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Chama a stored procedure para inserir os pontos na tabela grafico_saida
        EXEC InserirGraficoSaida @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;

        FETCH NEXT FROM simulacoes_cursor INTO @idSimulacao, @frequencia, @comprimentoOnda, @erroMax;
    END

    CLOSE simulacoes_cursor;
    DEALLOCATE simulacoes_cursor;
END;
GO
