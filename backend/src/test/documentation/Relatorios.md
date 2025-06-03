***O conteúdo apresentado pode passar por alterações ou estar desatualizado. Caso necessário, entre em contato com o autor do repositório.***

# Matriz de Rastreabilidade

| **Critério**                 | **Descrição**                                                        | **Casos de Teste**     |
|------------------------------|----------------------------------------------------------------------|-------------------------|
| ID de Cliente                | Validar se está entre 1 e 99999                                     | CT 1               |
| E-mail                       | Verificar se possui "@" e "." no formato                             | CT 2, CT 3              |
| Data de Nascimento           | Formato YYYYMMDD, ano ≥1900 e não pode ser futura                    | CT 4, CT 5              |
| Cliente Existente            | Cliente deve existir para realizar operações                         | CT 6, CT 7              |
| ID de Conta                  | Validar se está entre 100001 e 999999                                | CT 8, CT 9             |
| Tipo de Conta                | Deve ser "S" (Poupança) ou "C" (Corrente)                            | CT 10, CT 11            |
| Valor da Transação           | Valor deve ser maior que 0 e até 999.999,99                          | CT 12, CT 13            |
| Saldo para Saque             | Saldo deve ser igual ou maior que o valor solicitado                 | CT 14                   |
| Datas de Relatório           | Formato correto, inicial ≤ final, e nenhuma no futuro                | CT 15, CT 16            |

---

[🏠 README](../../../../README.md)

# Cenários Testados
## 1. Gerenciamento de Clientes
**Cenário 1:** Cadastro de um novo usuário com dados válidos (ID);
- Descrição: Verificar cadastro de cliente com ID dentro do intervalo permitido.
- Status: **Aprovado**
<img src="evidence\1-2-4.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 2:** Cadastro de um novo usuário com dados válidos (Email);
- Descrição: Verificar cadastro de cliente com e-mail no formato correto.
- Status: **Aprovado**
<img src="evidence\1-2-4.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 3:** Tentativa de cadastro com dados inválidos(Email);
- Descrição: Validar rejeição ao cadastrar cliente com e-mail inválido.
- Status: **Aprovado**
<img src="evidence\3.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 4:** Cadastro de um novo usuário com dados válidos (Data - YYYYMMDD);
- Descrição: Verificar cadastro com data de nascimento válida e formato correto.
- Status: **Aprovado**
<img src="evidence\1-2-4.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 5:** Tentativa de cadastro com dados inválidos (Data - YYYYMMDD); 
- Descrição: Validar rejeição ao cadastrar com data inválida ou no futuro.
- Status: **Aprovado**
<img src="evidence\5.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 6:** Realizar uma operação (Deposito, Saque) utilizando um cliente válido;
- Descrição: Validar operação (depósito ou saque) com cliente existente.
- Status: **Aprovado**
<img src="evidence\6-11.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 7:** Tentativa de realizar uma operação (Deposito, Saque) utilizando um cliente inválido;
- Descrição: Verificar rejeição na operação utilizando cliente inexistente.
- Status: **Aprovado**
<img src="evidence\7.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 8:** Cadastro de uma nova conta com dados válidos (ID e Tipo)
- Descrição: Verificar cadastro de conta com ID dentro do intervalo permitido.
- Status: **Aprovado**
<img src="evidence\8.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 9:** Tentativa de cadastro com ID de conta inválido
- Descrição: Validar rejeição ao cadastrar conta com ID fora do limite.
- Status: **Aprovado**
<img src="evidence\9.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 10:** Tentativa de cadastro com tipo de conta inválido
- Descrição: Verificar cadastro de conta com tipo válido ("S" ou "C").
- Status: **Aprovado**
<img src="evidence\10.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 11:** Realizar transação com valor válido e saldo suficiente
- Descrição: 	Validar rejeição ao cadastrar conta com tipo inválido.
- Status: **Aprovado**
<img src="evidence\6-11.png" alt="Descrição da imagem" width="350" height="150"/>

---

**Cenário 12:** Realizar transação com valor inválido
- Descrição: Verificar transação com valor dentro dos limites aceitos.
- Status: **Aprovado**
<img src="evidence\12.png" alt="Descrição da imagem" width="350" height="100"/>

---

**Cenário 13:** Realizar saque com saldo insuficiente
- Descrição: Validar rejeição de transação com valor inválido (zero ou excedente).
- Status: **Aprovado**
<img src="evidence\12-13.png" alt="Descrição da imagem" width="350" height="100"/>

---

**Cenário 14:** Geração de relatório com dados válidos
- Descrição: Verificar rejeição de saque quando saldo é insuficiente.
- Status: **Aprovado**
<img src="evidence\14.png" alt="Descrição da imagem" width="305" height="200"/>

---

**Cenário 15:** Tentativa de gerar relatório com datas inválidas
- Descrição: Verificar geração de relatório com datas válidas e ordem correta.
- Status: **Aprovado**
<img src="evidence\15_Reprovado.png" alt="Descrição da imagem" width="305" height="200"/>

---

**Cenário 16:** Operação em conta inexistente
- Descrição: Validar rejeição de relatório com datas inválidas ou no futuro.
- Status: **Aprovado**
<img src="evidence\16.png" alt="Descrição da imagem" width="350" height="150"/>

---
