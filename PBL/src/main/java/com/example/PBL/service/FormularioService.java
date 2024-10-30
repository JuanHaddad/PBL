package com.example.PBL.service;

import com.example.PBL.model.Formulario;
import com.example.PBL.repositorio.FormularioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormularioService {

    private final FormularioRepositorio formularioRepositorio;

    @Autowired
    public FormularioService(FormularioRepositorio formularioRepositorio) {
        this.formularioRepositorio = formularioRepositorio;
    }

    public Formulario salvarFormulario(Formulario formulario) {
        return formularioRepositorio.save(formulario);
    }

    public List<Formulario> listarFormularios() {
        return formularioRepositorio.findAll();
    }
}
