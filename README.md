# Sistema Bancário Moderno: Migração de COBOL Legado para Spring Boot

Um sistema bancário abrangente que moderniza aplicações COBOL legadas fornecendo um backend Spring Boot e frontend Angular, mantendo compatibilidade com sistemas COBOL. O sistema permite gerenciamento de clientes, operações de conta e relatórios de transações através de uma interface web moderna.

Este projeto preenche a lacuna entre sistemas bancários legados e aplicações web modernas implementando uma abordagem de arquitetura dual. O sistema mantém a funcionalidade COBOL original enquanto fornece uma API REST moderna e interface web. Suporta operações bancárias essenciais incluindo registro de clientes, gerenciamento de contas, depósitos, saques e relatórios de transações.

Principais recursos incluem:
- Gerenciamento de clientes (registro, atualizações e recuperação de informações)
- Operações de conta (criação, depósitos e saques)
- Relatórios de transações com filtragem por intervalo de datas
- Interface web moderna construída com Angular Material
- API RESTful com documentação OpenAPI
- Banco de dados H2 em memória para desenvolvimento e testes
- Suporte a Docker para componentes COBOL

## Estrutura do Repositório
```
.
├── backend/                 # Aplicação backend Spring Boot
│   ├── src/
│   │   ├── main/           # Código fonte da aplicação
│   │   │   ├── java/      # Arquivos fonte Java
│   │   │   └── resources/ # Propriedades e configurações da aplicação
│   │   └── test/          # Casos de teste e documentação
│   └── pom.xml            # Configuração do projeto Maven
├── cobol/                  # Componentes da aplicação COBOL legada
│   ├── ACCTMGMT.cbl       # Programa de gerenciamento de contas
│   ├── CUSTINFO.cbl       # Manipulação de informações do cliente
│   ├── CUSTREG.cbl        # Programa de registro de clientes
│   ├── MAINMENU.cbl       # Interface do menu principal
│   ├── TXNREPT.cbl        # Relatórios de transações
│   └── Dockerfile         # Configuração do ambiente COBOL
└── frontend/              # Aplicação frontend Angular
    ├── src/
    │   └── app/          # Componentes e serviços Angular
    ├── angular.json      # Configuração do Angular CLI
    └── package.json      # Dependências e scripts NPM
```

## Instruções de Uso
### Pré-requisitos
- Java Development Kit (JDK) 17
- Node.js 14+ e npm
- Maven 3.6+
- Compilador GnuCOBOL (para componentes COBOL)
- Docker (opcional, para ambiente COBOL em contêiner)

### Instalação

#### Backend (Spring Boot)
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Frontend (Angular)
```bash
cd frontend
npm install
ng serve
```

#### Componentes COBOL (Usando Docker)
```bash
cd cobol
docker build -t banking-cobol .
docker run -it banking-cobol
```

### Início Rápido
1. Inicie o servidor backend:
```bash
cd backend
mvn spring-boot:run
```

2. Inicie a aplicação frontend:
```bash
cd frontend
ng serve
```

3. Acesse a aplicação em `http://localhost:4200`

### API e Documentação

A API do backend pode ser acessada e testada através do Swagger UI disponível em:

```
http://localhost:8080/swagger
```

O Swagger UI fornece documentação interativa completa de todas as rotas da API, incluindo:
- Endpoints disponíveis
- Parâmetros necessários
- Formatos de resposta
- Funcionalidade de teste integrada

Para testar a API de relatório de transações:
1. Acesse o Swagger UI no navegador
2. Localize o endpoint `/api/transactions/report/{customerId}`
3. Forneça o ID do cliente e o intervalo de datas
4. Execute a requisição para visualizar os IDs de cliente e transação nos resultados

### Solução de Problemas

#### Problemas Comuns

1. Backend falha ao iniciar
- Erro: "Porta 8080 já em uso"
  - Solução: Encerre o processo usando a porta 8080 ou altere a porta em `application.properties`
  ```properties
  server.port=8081
  ```

2. Problemas de conexão com o banco de dados
- Erro: "Não foi possível conectar ao banco de dados H2"
  - Solução: Verifique as configurações do console H2 em `application.properties`
  ```properties
  spring.h2.console.enabled=true
  spring.datasource.url=jdbc:h2:mem:bankingdb
  ```

3. Integração COBOL
- Erro: "Programa COBOL não encontrado"
  - Solução: Verifique se os arquivos COBOL estão compilados corretamente
  ```bash
  cobc -x MAINMENU.cbl CUSTREG.o ACCTMGMT.o TXNREPT.o CUSTINFO.o -o menu
  ```

## Fluxo de Dados
O sistema implementa uma arquitetura em camadas onde os dados fluem da interface do usuário através da API REST para os serviços de backend e banco de dados.

```ascii
[Interface do Usuário] <-> [Frontend Angular] <-> [API Spring Boot] <-> [Camada de Serviço] <-> [Banco de Dados]
                                                                    <-> [Integração COBOL]
```

Interações de componentes:
1. Componentes frontend fazem requisições HTTP para a API REST
2. Controladores validam requisições e delegam para serviços apropriados
3. Serviços implementam lógica de negócios e transformações de dados
4. Repositórios lidam com persistência de dados
5. Programas COBOL lidam com operações legadas através de E/S de arquivos
6. Dados são sincronizados entre sistemas modernos e legados
7. Respostas fluem de volta através das mesmas camadas com transformações apropriadas

## Infraestrutura

![Diagrama de infraestrutura](./docs/infra.svg)
O sistema usa Docker para implantação de componentes COBOL:

### Recursos Docker
- Imagem Base: Ubuntu 20.04
- Pacotes Instalados:
  - Compilador GnuCOBOL
  - GCC
  - Ferramentas de desenvolvimento necessárias
- Diretório de Trabalho: /cobol
- Arquivos de Dados: CUSTOMER.dat, ACCOUNT.dat, TRANSACT.dat
- Programas Compilados: menu (executável principal)

## Implantação
1. Pré-requisitos:
   - Docker instalado
   - Ambiente Java 17
   - Node.js e npm

2. Construir e implantar:
```bash
# Backend
cd backend
mvn clean package
java -jar target/banking-system.jar

# Frontend
cd frontend
npm run build
# Implante a pasta dist no servidor web

# COBOL
cd cobol
docker build -t banking-cobol .
docker run -d banking-cobol
```

## Testes
Para executar os testes da aplicação:

```bash
# Testes do Backend
cd backend
mvn test

# Testes do Frontend
cd frontend
ng test
```

Os testes incluem:
- Testes unitários para serviços e controladores
- Testes de integração para APIs
- Testes de componentes para o frontend