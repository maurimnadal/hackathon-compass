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

export class ListarCliente {
  clienteId: number | null = null;
  buscou = false;
  clienteSelecionado: Cliente | null = null;
  mensagem: string = '';

  constructor(private clienteService: ClienteService) {}

  buscarCliente() {
    this.buscou = true;
    this.clienteSelecionado = null;
    this.mensagem = ''


    if (this.clienteId !== null && this.clienteId > 0) {
      this.clienteService.getClient(this.clienteId).subscribe({
        next: (data) => {
          if (data) {
            this.clienteSelecionado = data;
            this.mensagem = '';
          } else {
            this.mensagem = 'Cliente não encontrado.';
          }
        },
        error: (err) => {
          this.clienteSelecionado = null;
          this.mensagem = 'Cliente não encontrado.';
          console.error('Erro ao buscar cliente:', err);
        }
      });
    } else {
      this.mensagem = 'Digite um ID válido.';
    }
  }
}