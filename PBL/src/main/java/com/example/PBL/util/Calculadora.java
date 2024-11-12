package com.example.PBL.util;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Calculadora {

    public double calcularSenoTaylor(double valor, double erroMaximo) {
        valor = valor % (2 * Math.PI);
        if (valor > Math.PI) valor -= 2 * Math.PI;
        else if (valor < -Math.PI) valor += 2 * Math.PI;

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

    public Map<String, List<Double>> calcularPontosOnda(double frequencia, double comprimentoOnda, double tempo, double erroMaximo) {
        List<Double> dadosX = new ArrayList<>();
        List<Double> dadosY = new ArrayList<>();
        double passoX = 0.01;

        for (double x = 0; x <= 2; x += passoX) {
            double argumento = 2 * Math.PI * (frequencia * tempo - x / comprimentoOnda);
            double y = calcularSenoTaylor(argumento, erroMaximo);
            dadosX.add(x);
            dadosY.add(y);
        }

        Map<String, List<Double>> resultado = new HashMap<>();
        resultado.put("dadosX", dadosX);
        resultado.put("dadosY", dadosY);
        return resultado;
    }

    public double calcularValorNoPonto(double frequencia, double comprimentoOnda, double tempo, double erroMaximo) {
        double posicaoX = 1;
        double argumento = 2 * Math.PI * (frequencia * tempo - posicaoX / comprimentoOnda);
        return calcularSenoTaylor(argumento, erroMaximo);
    }
}
