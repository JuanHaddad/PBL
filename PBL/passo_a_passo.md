Pra funcionar esse projeto, deve estar instalado e devidamente configurado o sql server

IntelliJ: Vá em File > Invalidate Caches / Restart e reinicie o projeto.

Pra isso baixe o pacote do sql management nesse link:
https://aka.ms/ssmsfullsetup

Na instalação tem uma parte que é para verificar
o tipo de Login (Autenticação), escolha
SQLServer  + Windows

Usuário sempre será SA
Senha: 123456

---------------------------------------------------
Após isso, certifique-se de ter o java 17 instalado no computador (JDK 17)

Se não tiver, o proprio intellij vai reclamar e vai dar a opção de você instalar via intellij mesmo

Abra o seguinte app: 
#### SQL SERVER 2022 CONFIGURATION MANAGER

lá você vai habilitar a conexão tcp/ip do seu banco
