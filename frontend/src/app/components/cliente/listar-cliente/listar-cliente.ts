import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { Customer } from '../../../models/customer/customer';
import { AccountService } from '../../../services/account/account.service';
@Component({
  selector: 'app-listar-cliente',
  standalone: true,
  imports: [CommonModule, MatCardModule, FormsModule, MatFormFieldModule, MatInputModule],
  templateUrl: './listar-cliente.html',
  styleUrl: './listar-cliente.css'
})

export class ListarCliente {
  customerId: number | null = null;
  buscou = false;
  customerSelecionado: Customer | null = null;
  mensagem: string = '';

  constructor(private accountService: AccountService) {}

  buscarCustomer() {
    this.buscou = true;
    this.customerSelecionado = null;
    this.mensagem = ''


    if (this.customerId !== null && this.customerId > 0) {
      this.accountService.getCustomerAccounts(this.customerId).subscribe({
        next: (data: Customer | null) => {
          if (data) {
            this.customerSelecionado = data;
            
            this.mensagem = '';
          } else {
            this.mensagem = 'Cliente não encontrado.';
          }
        },
        error: (err: any) => {
          this.customerSelecionado = null;
          if (err.status === 404) {
            this.mensagem = 'Cliente não encontrado ou não possui contas.';
          } else {
            this.mensagem = 'Erro ao buscar cliente.';
          }
          console.error('Erro ao buscar cliente:', err);
        }
      });
    } else {
      this.mensagem = 'Digite um ID válido.';
    }
  }
}