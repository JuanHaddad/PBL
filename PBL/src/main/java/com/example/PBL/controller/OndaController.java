package com.example.PBL.controller;

import com.example.PBL.model.SimuladorOnda;
import com.example.PBL.model.SimuladorOnda.PontoOnda;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OndaController {

    private final SimuladorOnda simuladorOnda;

    public OndaController(SimuladorOnda simuladorOnda) {
        this.simuladorOnda = simuladorOnda;
    }

    @GetMapping("/simularOnda")
    public String simularOnda(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("duracao") double duracao,
            @RequestParam("erroMaximo") double erroMaximo,
            Model model
    ) {
        List<PontoOnda> pontos = simuladorOnda.calcularOnda(frequencia, comprimentoOnda, duracao, erroMaximo);
        model.addAttribute("pontos", pontos);
        return "grafico";
    }
    @GetMapping("/formulario")
    public String exibirFormulario() {
        return "formulario";  // Nome do template (sem a extensão .html)
    }
}
