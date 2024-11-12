package com.example.PBL.controller;

import com.example.PBL.util.Calculadora;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CalculadoraController {

    private final Calculadora calculadora;

    public CalculadoraController(Calculadora calculadora) {
        this.calculadora = calculadora;
    }

    @GetMapping("/calcularPontosOnda")
    public Map<String, List<Double>> calcularPontosOnda(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("tempo") double tempo,
            @RequestParam("erroMaximo") double erroMaximo
    ) {
        return calculadora.calcularPontosOnda(frequencia, comprimentoOnda, tempo, erroMaximo);
    }

    @GetMapping("/calcularValorNoPonto")
    public double calcularValorNoPonto(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("tempo") double tempo,
            @RequestParam("erroMaximo") double erroMaximo
    ) {
        return calculadora.calcularValorNoPonto(frequencia, comprimentoOnda, tempo, erroMaximo);
    }
}
