package com.example.PBL.model;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component  // Adiciona esta anotação
public class SimuladorOnda {

    public static class PontoOnda {
        public double x;
        public double y;

        public PontoOnda(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public List<PontoOnda> calcularOnda(double frequencia, double comprimentoOnda, double duracao, double erroMaximo) {
        List<PontoOnda> pontos = new ArrayList<>();
        double passoX = 0.01; // Espaçamento de 1 cm (0,01 metros)
        double passoT = 0.05; // 50 ms (0,05 segundos)
        int numPontos = (int) (1 / passoX); // Corda de 1 metro
        int numTempos = (int) (duracao / passoT);

        for (int tIndex = 0; tIndex < numTempos; tIndex++) {
            double t = tIndex * passoT;
            for (int xIndex = 0; xIndex <= numPontos; xIndex++) {
                double x = xIndex * passoX;
                double y = calcularSenoTaylor(2 * Math.PI * (frequencia * t - (x / comprimentoOnda)), erroMaximo);
                pontos.add(new PontoOnda(x, y));
            }
        }
        return pontos;
    }

    private double calcularSenoTaylor(double valor, double erroMaximo) {
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
}
