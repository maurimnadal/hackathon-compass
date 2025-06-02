import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

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

@Component({
  selector: 'app-listar-cliente',
  standalone: true,
  imports: [CommonModule, MatCardModule, FormsModule, MatFormFieldModule, MatInputModule],
  templateUrl: './listar-cliente.html',
  styleUrl: './listar-cliente.css'
})

//verificar recebimento do formato da data
export class ListarCliente {
  clienteId: number | null = null;
  buscou = false;
  clienteSelecionado: Cliente | null = null;

  isNegativo(saldo: any): boolean {
    return Number(saldo) < 0;
  }

  clientes: Cliente[] = [
    {
      id_cliente: 1,
      nome: 'João Silva',
      email: 'joao@email.com',
      data_nascimento: new Date(1990, 4, 15), // formato YYYY, MM (0-based), DD
      contas: [
        { id_conta: 101, tipo: 'Corrente', saldo: -1500.75 },
        { id_conta: 102, tipo: 'Poupança', saldo: 3200.00 },
        { id_conta: 101, tipo: 'Corrente', saldo: 1500.75 },
      ]
    }
  ];


  constructor() {
    // Converte data_nascimento para Date se vier como string YYYYMMDD
    this.clientes.forEach(cliente => {
      if (typeof cliente.data_nascimento === 'string') {
        const str = cliente.data_nascimento as string;
        cliente.data_nascimento = new Date(
          Number(str.substring(0, 4)),
          Number(str.substring(4, 6)) - 1,
          Number(str.substring(6, 8))
        );
      }
    });
  }

  buscarCliente() {
    this.buscou = true;
    this.clienteSelecionado = this.clientes.find(c => c.id_cliente === this.clienteId) || null;
  }
}