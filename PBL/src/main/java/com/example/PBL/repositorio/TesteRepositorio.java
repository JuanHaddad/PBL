package com.example.PBL.repositorio;

import com.example.PBL.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TesteRepositorio extends JpaRepository<Usuario, Long> {

    // ESSA INTERFACE É RESPONSÁVEL POR FAZER A COMUNICAÇÃO COM O BANCO DE DADOS, É UM DAO (DATA ACCESS OBJECT)
}
