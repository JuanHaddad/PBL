package com.example.PBL.controller;

import com.example.PBL.util.Calculadora;
import com.example.PBL.util.Calculadora.PontoOnda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OndaController {

    private final Calculadora calculadora;

    @Autowired
    public OndaController(Calculadora calculadora) {
        this.calculadora = calculadora;
    }

    @GetMapping("/simularOnda")
    public String simularOnda(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("duracao") double duracao,
            @RequestParam("erroMaximo") double erroMaximo,
<<<<<<< Updated upstream
            Model model
    ) {
        List<PontoOnda> pontos = simuladorOnda.calcularOnda(frequencia, comprimentoOnda, duracao, erroMaximo);
        model.addAttribute("pontos", pontos);
=======
            Model model) {

        List<PontoOnda> pontos = calculadora.calcularPontosOnda(frequencia, comprimentoOnda, duracao, erroMaximo);
        model.addAttribute("pontos", pontos);
        model.addAttribute("frequencia", frequencia);
        model.addAttribute("comprimentoOnda", comprimentoOnda);
        model.addAttribute("duracao", duracao);
        model.addAttribute("erroMaximo", erroMaximo);

>>>>>>> Stashed changes
        return "grafico";
    }
}
