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


// Controller que mapeia todos os endpoints possíveis, para retornar as telas corretas no thymeleaf
@Controller
public class OndaController {

    private final GraficoSaidaRepositorio graficoSaidaRepositorio;
    private final SimulacaoService simulacaoService;

    public OndaController(SimulacaoService simulacaoService, GraficoSaidaRepositorio graficoSaidaRepositorio) {
        this.simulacaoService = simulacaoService;
        this.graficoSaidaRepositorio = graficoSaidaRepositorio;
    }

    // Mapeia toda requisição no endpoint /simularOnda
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

        // retorna apenas a tela do gráfico
        return "grafico";
    }


    // Mapeamos o endpoint formulário apenas para retornar a tela do formulario.html
    @GetMapping("/formulario")
    public String exibirFormulario() {
        return "formulario";
    }



    // Mapeamos o endpoint de /resimularOnda para o histórico ter a opção de mostrar novamente a simulação através do id dela
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


        // Retornando também a tela do gráfico daqui
        return "grafico";
    }


    // Mapeando o endpoint para retornar ao menu principal
    @GetMapping("/")
    public String exibirMenuInicial() {
        return "menu";
    }

    // Mapeando o endpoint para ir ao historico
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

    @GetMapping("/excluirSimulacao")
    public String excluirSimulacao(@RequestParam("id_simulacao") Long idSimulacao) {
        simulacaoService.excluirSimulacao(idSimulacao);
        return "redirect:/historico";  // Redireciona de volta para a página do histórico
    }

    @GetMapping("/creditos")
    public String exibirCreditos() {
        return "creditos"; // Nome do arquivo HTML (creditos.html) para a página de créditos
    }

}
