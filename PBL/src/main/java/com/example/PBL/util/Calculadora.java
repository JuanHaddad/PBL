package com.example.PBL.util;

import org.springframework.stereotype.Component;

@Component
public class Calculadora {

    private static final double PASSO_X = 0.01;
    private static final double PI = Math.PI;

    /**
     * Função para calcular o valor do seno utilizando a série de Taylor
     *
     * @param valor       Valor do ângulo em radianos
     * @param erroMaximo  Erro máximo permitido para a aproximação
     * @return            O valor do seno aproximado pelo cálculo da série de Taylor
     */
    public double calcularSenoTaylor(double valor, double erroMaximo) {
        valor = valor % (2 * PI);
        if (valor > PI) valor -= 2 * PI;
        else if (valor < -PI) valor += 2 * PI;

        double termo = valor;
        double soma = termo;
        int n = 1;

        while (Math.abs(termo) > erroMaximo) {
            termo *= -1 * valor * valor / ((2 * n) * (2 * n + 1));
            soma += termo;
            n++;
        }
        return soma;
    }

    /**
     * Função para calcular os pontos da onda com base nos parâmetros fornecidos
     *
     * @param frequencia       Frequência da onda
     * @param comprimentoOnda  Comprimento da onda
     * @param t                Tempo atual
     * @param erroMaximo       Erro máximo permitido para a aproximação
     * @return                 Array contendo os valores X e Y da onda em um tempo específico
     */
    public double[][] calcularPontosOnda(double frequencia, double comprimentoOnda, double t, double erroMaximo) {
        int tamanho = (int) (2 / PASSO_X) + 1;
        double[][] pontos = new double[2][tamanho];
        int index = 0;

        for (double x = 0; x <= 2; x += PASSO_X) {
            double argumento = 2 * PI * (frequencia * t - x / comprimentoOnda);
            pontos[0][index] = x;  // Valor de X
            pontos[1][index] = calcularSenoTaylor(argumento, erroMaximo);  // Valor de Y
            index++;
        }
        return pontos;
    }

    /**
     * Função para calcular o valor da onda em um ponto X fixo para o tempo T
     *
     * @param frequencia       Frequência da onda
     * @param comprimentoOnda  Comprimento da onda
     * @param t                Tempo atual
     * @param erroMaximo       Erro máximo permitido para a aproximação
     * @return                 Valor Y da onda no ponto fixo X = 1
     */
    public double calcularValorNoPonto(double frequencia, double comprimentoOnda, double t, double erroMaximo) {
        double posicaoX = 1;
        double argumento = 2 * PI * (frequencia * t - posicaoX / comprimentoOnda);
        return calcularSenoTaylor(argumento, erroMaximo);
    }
}
