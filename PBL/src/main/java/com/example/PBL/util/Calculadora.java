package com.example.PBL.util;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class Calculadora {

    public double calcularSenoTaylor(double valor, double erroMaximo) {
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

    public List<PontoOnda> calcularPontosOnda(double frequencia, double comprimentoOnda, double duracao, double erroMaximo) {
        List<PontoOnda> pontos = new ArrayList<>();
        double passoX = 0.01;  // Espaçamento entre pontos (1 cm)
        double t = 0;
        double passoTempo = 0.0175;

        for (double x = 0; x <= 2; x += passoX) {
            double argumento = 2 * Math.PI * (frequencia * t - x / comprimentoOnda);
            double y = calcularSenoTaylor(argumento, erroMaximo);
            pontos.add(new PontoOnda(x, y));
        }
        return pontos;
    }

    public double calcularPosicaoPonto(double comprimentoOnda, double frequencia, double t) {
        return (t * comprimentoOnda * frequencia) % 2;
    }

    public double calcularValorNoPonto(double posicaoX, double t, double frequencia, double comprimentoOnda, double erroMaximo) {
        double argumento = 2 * Math.PI * (frequencia * t - posicaoX / comprimentoOnda);
        return calcularSenoTaylor(argumento, erroMaximo);
    }

    public static class PontoOnda {
        public double x;
        public double y;

        public PontoOnda(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
