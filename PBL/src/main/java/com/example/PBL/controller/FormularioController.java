package com.example.PBL.controller;

import com.example.PBL.model.Formulario;
import com.example.PBL.service.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FormularioController {

    private final FormularioService formularioService;

    @Autowired
    public FormularioController(FormularioService formularioService) {
        this.formularioService = formularioService;
    }

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("formulario", new Formulario());
        return "formulario";  // Nome do template do formulário
    }

    @PostMapping("/salvarFormulario")
    public RedirectView salvarFormulario(@ModelAttribute Formulario formulario) {
        formularioService.salvarFormulario(formulario);

        // Redireciona para a página de simulação com os parâmetros do formulário
        String redirectUrl = String.format("/simularOnda?frequencia=%s&comprimentoOnda=%s&duracao=%s&erroMaximo=%s",
                formulario.getFrequencia(),
                formulario.getComprimentoOnda(),
                formulario.getDuracao(),
                formulario.getErroMaximo());

        return new RedirectView(redirectUrl);
    }
}
