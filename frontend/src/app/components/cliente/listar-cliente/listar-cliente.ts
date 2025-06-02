import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Cliente } from '../../../models/cliente/cliente';
import { ClienteService } from '../../../services/cliente/cliente.service';

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

  clientes: Cliente[] = [];

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.clienteService.getClient(this.clienteId).subscribe({
      next: (data) => this.clientes = data,
      error: (err) => console.error('Erro ao buscar clientes:', err)
    });
  }


  /*constructor() {
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
  }*/

  buscarCliente() {
    this.buscou = true;
    this.clienteSelecionado = this.clientes.find(c => c.id_cliente === this.clienteId) || null;
  }
}