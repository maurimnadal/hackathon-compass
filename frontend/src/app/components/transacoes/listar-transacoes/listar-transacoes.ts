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
  transacoes: any[] = [];
  mensagem: string = '';

  constructor(private accountService: AccountService) {}

  listarTransacoes() {
    if (!this.numeroConta || !this.dataInicio || !this.dataFim) {
      this.mensagem = 'Preencha todos os campos.';
      return;
    }

    //traduzir para inglês caso necessário
    const body = {
      customerId: this.numeroConta,
      dataInicio: this.dataInicio.toISOString(),
      dataFim: this.dataFim.toISOString(),
    };

    /*this.http.post<any[]>('http://localhost:3000/transacoes', body).subscribe({
      next: (res) => {
        this.transacoes = res;
        this.mensagem = res.length === 0 ? 'Nenhuma transação encontrada.' : '';
      },
      error: () => {
        this.mensagem = 'Erro ao buscar transações.';
        this.transacoes = [];
      },
    });*/

    /*service*/

    this.accountService.getTransactions().subscribe({
      next: (res) => {
        this.transacoes = res;
        this.mensagem = res.length === 0 ? 'Nenhuma transação encontrada.' : '';
      },
      error: () => {
        this.mensagem = 'Erro ao buscar transações.';
        this.transacoes = [];
      },
    });
  }
}
