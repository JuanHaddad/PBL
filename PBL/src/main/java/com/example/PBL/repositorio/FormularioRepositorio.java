package com.example.PBL.repositorio;

import com.example.PBL.model.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormularioRepositorio extends JpaRepository<Formulario, Long> {
    // Métodos personalizados podem ser adicionados aqui, se necessário
}
