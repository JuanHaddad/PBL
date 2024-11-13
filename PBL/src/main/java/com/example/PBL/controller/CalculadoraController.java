package com.example.PBL.controller;

import com.example.PBL.util.Calculadora;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


// Decorator RestController para termos o nosso controller que retorna json
@RestController
public class CalculadoraController {

    private final Calculadora calculadora;

    public CalculadoraController(Calculadora calculadora) {
        this.calculadora = calculadora;
    }


//    Mapeamos qualquer get nessa url do /calcularPontosOnda para retornar o json com os pontos da onda no frontend
    @GetMapping("/calcularPontosOnda")
    public Map<String, List<Double>> calcularPontosOnda(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("tempo") double tempo,
            @RequestParam("erroMaximo") double erroMaximo
    ) {
        return calculadora.calcularPontosOnda(frequencia, comprimentoOnda, tempo, erroMaximo);
    }


    // Mapeamos /calcularValorNoPonto pra quando o usuário inputa um certo segundo pra ver o ponto na onda, o retorno é o calculo exato de onde o ponto está
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
