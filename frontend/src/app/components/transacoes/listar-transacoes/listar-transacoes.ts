import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AccountService } from '../../../services/account/account.service';
import { Account } from '../../../models/account/account';
import { Transaction } from '../../../models/transaction/transaction';

@Component({
  selector: 'app-listar-transacoes',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatDatepicker,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './listar-transacoes.html',
  styleUrls: ['./listar-transacoes.css'],
})

export class ListarTransacoes {
  private http = inject(HttpClient);

  numeroConta: string = '';
  dataInicio: Date | null = null;
  dataFim: Date | null = null;
  transacoes: Transaction | null = null;
  mensagem: string = '';

  constructor(private accountService: AccountService) {}

  listarTransacoes() {
    console.log('Campos:', this.numeroConta, this.dataInicio, this.dataFim);

    if (!this.numeroConta || !this.dataInicio || !this.dataFim) {
      this.mensagem = 'Preencha todos os campos.';
      console.log('Mensagem:', this.mensagem);
      return;
    }
    

    // Função para formatar a data para YYYYMMDD
    const formatDate = (date: Date) =>
      `${date.getFullYear()}${(date.getMonth() + 1).toString().padStart(2, '0')}${date.getDate().toString().padStart(2, '0')}`;

    const body = {
      customerId: Number(this.numeroConta),
      startDate: formatDate(this.dataInicio),
      endDate: formatDate(this.dataFim),
    };

    console.log('Enviando para API:', body);

    this.accountService.getTransactions({
      customerId: body.customerId,
      startDate: this.dataInicio,
      endDate: this.dataFim
    }).subscribe({
      next: (res) => {
        console.log('Resposta da API:', res);
        this.transacoes = res;
        if (!this.transacoes.transactions || this.transacoes.transactions.length === 0) {
          this.mensagem = 'Nenhuma transação encontrada.';
        } else {
          this.mensagem = '';
        }
      },
      error: (err) => {
        console.error('Erro ao buscar transações:', err);
        this.mensagem = 'Erro ao buscar transações.';
        this.transacoes = null;
      },
    });
  }
}
