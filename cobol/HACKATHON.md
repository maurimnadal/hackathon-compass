# 🧠 Hackathon COBOL to Modern Stack (Java + Angular)

## 🎯 Contexto

Você faz parte de um time encarregado de modernizar um sistema legado escrito em **COBOL**, composto por quatro programas principais:

- `MAINMENU` – Menu principal  
- `CUSTREG` – Cadastro de Clientes  
- `ACCTMGMT` – Gerenciamento de Contas  
- `TXNREPT` – Relatório de Transações  

Esses programas se comunicam por meio de arquivos texto (flat files) e simulam um pequeno sistema bancário. Sua missão é migrar esse sistema para uma stack moderna utilizando **Java** no back-end e **Angular** no front-end, com possibilidade de adoção de microserviços e microfrontends.

---

## 📌 Objetivo Geral

Modernizar os programas COBOL para uma arquitetura moderna em camadas, aplicando conceitos como microserviços, interfaces web com Angular, boas práticas de desenvolvimento e testes automatizados.

---

## 🧭 Missão dos Times

Durante os dias de hackathon, cada time deverá:

1. **Recriar as funcionalidades dos programas COBOL utilizando Java e Angular.**
2. Manter as funcionalidades originais do programa COBOL.
3. Aplicar **boas práticas de validação de dados** no front-end e no back-end.
4. Desenvolver **testes unitários**. Sugestão JUnit (Java) e Jasmine/Karma (Angular).
5. Criar uma interface funcional que simule os menus e telas do sistema COBOL.
6. Substituir arquivos flat por outra forma de persistência. Dica: poder ser um banco de dados em memória.
7. Documentar o projeto (README com instruções de execução, arquitetura e estrutura).
8. **Dividir o back-end em microserviços**, por domínio, se entenderem ser adequado (ex: serviço de contas, serviço de clientes).
9. **Criar microfrontends**, se identificado que a separação da interface em domínios distintos pode beneficiar a modularidade da aplicação.
10. Adicionar sistema basico de **login**.

---

## 🛠️ Stack Sugerida

- **Back-end:** Java, Spring Boot, Maven/Gradle  
- **Front-end:** Angular  
- **Persistência:** Banco em memória (ex: H2)  
- **Testes:** JUnit (Java), Jasmine/Karma (Angular)  
- **Arquitetura:** Microserviços no back-end e microfrontends, se aplicável  

---

## 🚀 Entrega Final

Cada time deve entregar:

- ✅ Repositório no GitHub com:
  - README com explicações e instruções
  - Estrutura organizada de front e back-end
  - Testes implementados
  - Código-fonte limpo e funcional
- ✅ Demonstração do sistema e do código (apresentação ao vivo)

---

## 💡 Dicas

- Dividam o trabalho desde o início (front, back, testes, documentação).
- Comecem com as funcionalidades principais (ex: cadastro).
- Validem campos obrigatórios e formatos de dados.
- Evitem acoplamento entre microserviços – usem DTOs e boas práticas REST.
- Pensem na experiência do usuário ao navegar entre as telas.
- Se possível, usem Angular Modules para estruturar bem o projeto.

Boa sorte e boa modernização! 🚀
