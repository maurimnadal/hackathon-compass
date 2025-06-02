import { Conta } from "../conta/conta";

export interface Cliente {
  id_cliente: number;
  nome: string;
  email: string;
  data_nascimento: Date;
  contas: Conta[];
}