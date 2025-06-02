export interface Conta {
  id_conta: number;
  tipo: string;
  saldo: number;
}

export interface Cliente {
  id_cliente: number;
  nome: string;
  email: string;
  data_nascimento: Date;
  contas: Conta[];
}