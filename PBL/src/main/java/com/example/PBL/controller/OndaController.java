package com.example.PBL.controller;

import com.example.PBL.model.Simulacao;
import com.example.PBL.service.SimulacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.PBL.model.GraficoSaida;
import com.example.PBL.repositorio.GraficoSaidaRepositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OndaController {

    private final GraficoSaidaRepositorio graficoSaidaRepositorio;
    private final SimulacaoService simulacaoService;

    public OndaController(SimulacaoService simulacaoService, GraficoSaidaRepositorio graficoSaidaRepositorio) {
        this.simulacaoService = simulacaoService;
        this.graficoSaidaRepositorio = graficoSaidaRepositorio;
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
        simulacao = simulacaoService.salvarSimulacao(simulacao);

        // Adiciona os parâmetros ao modelo para passá-los ao Thymeleaf
        model.addAttribute("id_simulacao", simulacao.getId_simulacao());
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
    public String resimularOnda(@RequestParam("id_simulacao") Long id_simulacao, Model model) {
        Simulacao simulacao = simulacaoService.buscarSimulacaoPorId(id_simulacao);
        if (simulacao != null) {
            model.addAttribute("id_simulacao", id_simulacao);
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


    @GetMapping("/pontoNoTempo")
    @ResponseBody
    public Map<String, Object> obterPontoNoTempo(@RequestParam("id_simulacao") Long idSimulacao, @RequestParam("tempo") double tempo) {
        // Busca o ponto de saída exato
        Double posicaoY = graficoSaidaRepositorio.findByIdSimulacaoAndPontoTempoExato(idSimulacao, tempo)
                .map(GraficoSaida::getPontoPosicaoY)
                .orElse(null);

        // Retorna um Map com a resposta em JSON
        Map<String, Object> response = new HashMap<>();
        response.put("posicaoY", posicaoY);
        return response;
    }

}
