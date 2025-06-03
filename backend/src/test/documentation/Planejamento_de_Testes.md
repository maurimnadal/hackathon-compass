# Planejamento de Teste

---

### Nome do Projeto
Hackathon Compass
### Resumo
O hackathon tem como objetivo trazer uma solução de problemas de modernização de código, no caso deste projeto, Cobol para Java/Sping Boot
### Funcionalidades ou Módulos a serem Testados
- Enpoint /clientes
    - Cadastro de usuário
    - Gerenciamento dos usuários
- Endpoint /contas
    - Cadastro de conta
    - Gerenciamento da conta (Deposito, Saque)
    - Consulta de dados da conta

### Local dos Testes
- Ambiente de Teste: Ambiente Virtual, etapa de testes
- Ferramentas: 

### Critérios de Aceitação
1. **Validação de IDs:**
    - IDs de cliente devem estar entre 1 e 99999
    - IDs de conta devem estar entre 100001 e 999999
2. **Validação de Tipos de Conta:**
    - Contas devem ser do tipo "S" (poupança) ou "C" (corrente)
3. **Validação de Transações:**
    - Valores de transação devem ser maiores que zero
    - Valores de transação não podem exceder 999.999,99
    - Saques exigem saldo suficiente na conta
4. **Validação de Dados Pessoais:**
    - Emails devem ter formato válido (com @ e pelo menos um ponto)
    - Datas de nascimento devem estar no formato YYYYMMDD
    - Datas de nascimento não podem estar no futuro
    - Anos de nascimento devem estar entre 1900 e o ano atual
5. **Validação de Relatórios:**
    - Datas de relatório devem estar no formato YYYYMMDD
    - Data inicial deve ser anterior ou igual à data final
    - Datas de relatório não podem estar no futuro
    - Apenas transações do cliente especificado são exibidas
6. **Verificação de Existência:**
    - Clientes e contas devem existir para operações relacionadas
    - Mensagens apropriadas são exibidas quando registros não são encontrados

### Riscos
Evento futuro que tem uma propabilidade de ocorrência e que tem um potencial de perda, além de catalogar os riscos, maneiras para mitigar esse problema devem ser criadas.
- **Risco 1: Segurança**
    - Acesso Acesso não autorizado aos dados de um usuário
    - Mitigação: Autenticação de dois fatores, criptografia de dados
    - Impacto: Comprometimento de dados sensíveis, resultando em perda de confiança e possiveis implicações legais
- **Risco 2: Disponibilidade**
    - Sistema fora do ar, impossibilitando o acesso ao cadastro, gerenciamento e consulta dos usuários
    - Mitigação: Monitoramento constante do sistema, implementação de redundância de servidores
    - Impacto: Interrupção dos serviços, resultado em insatisfação do cliente e possível perda de receita

### Cenários Macro na Suíte
#### 1 - Gerenciamento de Clientes
- **Cenário 1:** Cadastro de um novo usuário com dados válidos (ID);
    - **Pré-condição:** Nenhum cliente cadastrado com esse ID.
    - **Ação:** Informar ID de cliente válido (entre 1 e 99999).
    - **Resultado esperado:** Cliente cadastrado com sucesso.
    
- **Cenário 2:** Cadastro de um novo usuário com dados válidos (Email);
    - **Pré-condição:** Cliente com ID válido.
    - **Ação:** Informar e-mail no formato válido (possui "@" e pelo menos um ".").
    - **Resultado esperado:** Cliente cadastrado com sucesso.

- **Cenário 3:** Tentativa de cadastro com dados inválidos(Email);
    - **Pré-condição:** Cliente com ID válido.
    - **Ação:** Informar e-mail sem "@" ou sem ponto.
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "E-mail inválido".

- **Cenário 4:** Cadastro de um novo usuário com dados válidos (Data - YYYYMMDD);
    - **Pré-condição:** Cliente com ID válido.
    - **Ação:** Informar data de nascimento no formato YYYYMMDD, com ano entre 1900 e o ano atual, e que não seja uma data futura.
    - **Resultado esperado:** Cliente cadastrado com sucesso.

- **Cenário 5:** Tentativa de cadastro com dados inválidos (Data - YYYYMMDD);    
    - **Pré-condição:** Cliente com ID válido.
    - **Ação:** Informar data fora do formato YYYYMMDD, ou com ano fora do intervalo (antes de 1900 ou depois do ano atual), ou uma data futura.
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Data de nascimento inválida".

- **Cenário 6:** Realizar uma operação (Deposito, Saque) utilizando um cliente válido;
    - **Pré-condição:** Cliente e conta existentes, conta com saldo suficiente (para saque).
    - **Ação:** Realizar depósito ou saque para a conta vinculada ao cliente existente, utilizando valores válidos (maior que 0 e menor ou igual a 999.999,99).
    - **Resultado esperado:** Operação realizada com sucesso.

- **Cenário 7:** Tentativa de realizar uma operação (Deposito, Saque) utilizando um cliente inválido;
    - **Pré-condição:** Cliente não existe no sistema.
    - **Ação:** Tentar realizar depósito ou saque utilizando um ID de cliente inexistente.
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Cliente não encontrado".


#### 2 - Gerenciamento de Contas
- **Cenário 8:** Cadastro de uma nova conta com dados válidos (ID e Tipo)
    - **Pré-condição:** Cliente existente.
    - **Ação:** Informar ID de conta válido (entre 100001 e 999999) e tipo de conta válido ("C" ou "S").
    - **Resultado esperado:** Conta cadastrada com sucesso.

- **Cenário 9:** Tentativa de cadastro com ID de conta inválido
    - **Pré-condição:** Cliente existente.
    - **Ação:** Informar ID de conta fora do intervalo permitido (menor que 100001 ou maior que 999999).
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "ID de conta inválido".

- **Cenário 10:** Tentativa de cadastro com tipo de conta inválido
    - **Pré-condição:** Cliente existente.
    - **Ação:** Informar tipo de conta diferente de "C" ou "S".
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Tipo de conta inválido".

- **Cenário 11:** Realizar transação com valor válido e saldo suficiente
    - **Pré-condição:** Conta existente e com saldo suficiente.
    - **Ação:** Realizar saque ou outro tipo de transação com valor maior que 0 e menor ou igual a 999.999,99.
    - **Resultado esperado:** Transação concluída com sucesso.

- **Cenário 12:** Realizar transação com valor inválido
    - **Pré-condição:** Conta existente.
    - **Ação:** Realizar transação com valor menor ou igual a 0 ou superior a 999.999,99.
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Valor de transação inválido".

- **Cenário 13:** Realizar saque com saldo insuficiente
    - **Pré-condição:** Conta existente com saldo inferior ao valor do saque.
    - **Ação:** Realizar saque.
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Saldo insuficiente para saque".

- **Cenário 14:** Geração de relatório com dados válidos
    - **Pré-condição:** Conta existente e transações registradas.
    - **Ação:** Informar data inicial e final no formato correto (YYYYMMDD), com a data inicial menor ou igual à data final, e nenhuma data no futuro.
    - **Resultado esperado:** Relatório gerado contendo as transações da conta.

- **Cenário 15:** Tentativa de gerar relatório com datas inválidas
    - **Pré-condição:** Conta existente.
    - **Ação:** Informar datas fora do padrão (formato incorreto, data inicial maior que a final ou datas futuras).
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Período de relatório inválido".

- **Cenário 16:** Operação em conta inexistente
    - **Pré-condição:** Conta não existente no sistema.
    - **Ação:** Tentar realizar qualquer operação (cadastro de transação, saque ou relatório).
    - **Resultado esperado:** Sistema rejeita e exibe a mensagem "Conta não encontrada".

---

[📊 Relatório de Testes](Relatorios.md)
[🏠 README](../../../../README.md)