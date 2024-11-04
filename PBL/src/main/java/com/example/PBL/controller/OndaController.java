package com.example.PBL.controller;

import com.example.PBL.model.Simulacao;
import com.example.PBL.service.SimulacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OndaController {

    private final SimulacaoService simulacaoService;

    public OndaController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @GetMapping("/simularOnda")
    public String simularOnda(
            @RequestParam("frequencia") double frequencia,
            @RequestParam("comprimentoOnda") double comprimentoOnda,
            @RequestParam("duracao") double duracao,
            @RequestParam("erroMaximo") double erroMaximo,
            Model model
    ) {
        // Cria um novo objeto Simulacao com os dados do formulário
        Simulacao simulacao = new Simulacao();
        simulacao.setFrequencia(frequencia);
        simulacao.setComprimentoOnda(comprimentoOnda);
        simulacao.setDuracao(duracao);
        simulacao.setErroMaximo(erroMaximo);

        // Salva a simulação usando o serviço
        simulacaoService.salvarSimulacao(simulacao);

        // Adiciona os parâmetros ao modelo para passá-los ao Thymeleaf
        model.addAttribute("frequencia", frequencia);
        model.addAttribute("comprimentoOnda", comprimentoOnda);
        model.addAttribute("duracao", duracao);
        model.addAttribute("erroMaximo", erroMaximo);

        // Recupera o histórico de simulações
//        List<Simulacao> historicoSimulacoes = simulacaoService.buscarTodasSimulacoes();
//        model.addAttribute("historicoSimulacoes", historicoSimulacoes);

        return "grafico";
    }

    @GetMapping("/formulario")
    public String exibirFormulario() {
        return "formulario";
    }


    @GetMapping("/resimularOnda")
    public String resimularOnda(@RequestParam("id") Long id, Model model) {
        Simulacao simulacao = simulacaoService.buscarSimulacaoPorId(id);
        if (simulacao != null) {
            model.addAttribute("frequencia", simulacao.getFrequencia());
            model.addAttribute("comprimentoOnda", simulacao.getComprimentoOnda());
            model.addAttribute("duracao", simulacao.getDuracao());
            model.addAttribute("erroMaximo", simulacao.getErroMaximo());
        }

        List<Simulacao> historicoSimulacoes = simulacaoService.buscarTodasSimulacoes();
        model.addAttribute("historicoSimulacoes", historicoSimulacoes);

        return "grafico";
    }
    @GetMapping("/")
    public String exibirMenuInicial() {
        return "menu";
    }
    @GetMapping("/historico")
    public String exibirHistorico(Model model) {
        List<Simulacao> historicoSimulacoes = simulacaoService.buscarTodasSimulacoes();
        model.addAttribute("historicoSimulacoes", historicoSimulacoes);
        return "historico";
    }

}
