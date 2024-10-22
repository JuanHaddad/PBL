package com.example.PBL.controller;

import com.example.PBL.model.Usuario;
import com.example.PBL.repositorio.TesteRepositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TesteController {

    private final TesteRepositorio testeRepositorio;

    public TesteController(TesteRepositorio testeRepositorio) {
        this.testeRepositorio = testeRepositorio;
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = testeRepositorio.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    @GetMapping("/adicionarUsuario")
    public String exibirFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "adicionarUsuario";
    }

    @PostMapping("/salvarUsuario")
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        testeRepositorio.save(usuario);
        return "redirect:/usuarios";
    }

    // Novo método para deletar usuário por ID
    @GetMapping("/deletarUsuario/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        testeRepositorio.deleteById(id);  // Exclui o usuário com o ID especificado
        return "redirect:/usuarios";  // Redireciona de volta para a página de listagem
    }
}
