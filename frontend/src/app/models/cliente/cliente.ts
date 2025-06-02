import { Conta } from "../conta/conta";

export interface Cliente {
  id_cliente: number | null;
  nome: string;
  email: string;
  data_nascimento: Date;
  contas: Conta[];
}