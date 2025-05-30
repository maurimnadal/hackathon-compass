import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

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
  numeroConta: string = '';
  dataInicio: Date | null = null;
  dataFim: Date | null = null;
  transacoes: any[] = [];
  mensagem: string = '';

  listarTransacoes() {
    // Simulated response
    if (this.numeroConta === '123') {
      this.transacoes = [
        { id: '001', tipo: 'Depósito', valor: 1000, data: '2024-01-01' },
      ];
      this.mensagem = '';
    } else {
      this.transacoes = [];
      this.mensagem = 'Nenhuma transação encontrada para esse número de conta.';
    }
  }
}
