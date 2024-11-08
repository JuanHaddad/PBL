# Passo a Passo para Configuração do Projeto PBL

Este guia detalha as etapas necessárias para configurar e executar o projeto. Siga cada passo cuidadosamente para garantir o funcionamento adequado.

---

## 1. Pré-requisitos de Software

### 1.1 SQL Server 2022 e SQL Server Management Studio (SSMS)

1. **Baixe e instale o SQL Server Management Studio** através deste link:
   - [SSMS Full Setup](https://aka.ms/ssmsfullsetup)
2. **Durante a instalação**, será solicitada a configuração do tipo de Login (Autenticação). Escolha:
   - **SQL Server + Windows Authentication**
   - **Usuário:** `SA`
   - **Senha:** `123456`

3. **Habilite a Conexão TCP/IP**:
   - Abra o aplicativo **SQL Server 2022 Configuration Manager**.
   - Na aba de **Protocolos** do seu banco, habilite a conexão **TCP/IP** para permitir a conexão ao banco de dados.

### 1.2 JDK 17 (Java Development Kit)

1. **Instale o JDK 17**:
   - Certifique-se de que o **Java 17 (JDK 17)** está instalado no computador.
   - Caso não esteja, o IntelliJ IDE irá identificar e oferecer a opção de instalação automaticamente.

---

## 2. Configuração do Banco de Dados

1. **Abra o SQL Server Management Studio (SSMS)** e conecte-se ao servidor com as credenciais configuradas.

2. **Execute o Script de Criação de Banco de Dados (schema.sql)**:
   - **Antes de rodar o projeto**, execute apenas o comando para **criar o banco de dados**:
     ```sql
     CREATE DATABASE PBL_EC3;
     ```
   - Após o banco de dados ser criado, **rode o projeto** para que as tabelas sejam geradas automaticamente pelo Spring Boot.

3. **Configuração das Funções, Stored Procedures e Triggers**:
   - As funções, stored procedures e triggers são configuradas automaticamente ao iniciar o projeto Spring Boot, através da classe `DatabaseInitializer`. Isso garante que os cálculos e relacionamentos entre as tabelas `simulacoes` e `grafico_saida` sejam criados automaticamente.

---

## 3. Configuração e Execução do Projeto Spring Boot

1. **Abra o projeto no IntelliJ IDEA**.

2. **Configure a Conexão JDBC no `application.properties`**:
   - Verifique que a configuração do banco de dados em `application.properties` está correta para a conexão com o SQL Server, utilizando `SA` e `123456` como usuário e senha.

3. **Rode o `Main.java` para Inicializar o Projeto**:
   - Execute a classe `Main.java` para que o Spring Boot inicialize o projeto e crie as tabelas nativas.
   - **Atenção**: A classe `DatabaseInitializer` dentro do package '**config**', foi configurada para aguardar alguns segundos antes de executar as funções e procedures para garantir que o contexto do banco esteja totalmente configurado.

---

## 4. Resolução de Erros Comuns

### Erro ao executar `Main.java` do projeto Spring
Caso ocorra algum erro, revise os seguintes pontos:

- **SQL Server**:
   - Certifique-se de que os scripts de SQL foram executados corretamente. Em particular, rode os scripts de **Function**, **Stored Procedure** e **Trigger** no SQL Server.

- **Invalidate Caches / Restart no IntelliJ**:
   - No IntelliJ, vá em **File > Invalidate Caches / Restart** e reinicie o projeto para garantir que todas as configurações e caches foram atualizados.

---

## 5. Estrutura do Projeto

O projeto segue a seguinte estrutura:

- **controller**: Responsável pelo controle das requisições, contém a classe `OndaController`.
- **model**: Contém as classes de modelagem `Simulacao` e `SimuladorOnda`.
- **repositorio**: Contém a interface `SimulacaoRepositorio` para operações de banco de dados.
- **service**: Contém `SimulacaoService`, onde é implementada a lógica de serviço.
- **util**: Inclui a classe `Calculadora`, onde são realizados todos os cálculos do projeto.
- **Main.java**: Classe principal que inicializa a aplicação Spring Boot.

### Recursos Estáticos
- **static/css**: Arquivos CSS.
- **static/js**: Arquivos JavaScript.
- **templates**: Contém os templates Thymeleaf `menu.html`, `formulario.html`, `grafico.html`, `historico.html`, e `creditos.html`.

---

## 6. Executando o Projeto

1. **Abra o navegador** e acesse o endereço `http://localhost:8080` para visualizar a aplicação.
2. Use as páginas disponíveis para simular ondas, visualizar histórico, e acessar as demais funcionalidades.

---

### Observação
Mantenha o SQL Server e o IntelliJ configurados conforme os passos acima para garantir o funcionamento ideal do projeto.

---
