import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ContaService } from '../../../services/conta/conta.service';

@Component({
  selector: 'app-depositar',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './depositar.html',
  styleUrl: './depositar.css'
})
export class Depositar {
  numeroConta: number | null = null;
  valor: number | null = null;
  mensagem: string = '';


  constructor(private contaService: ContaService) { }

  depositar() {
    // Validação do valor e número da conta
    if (!this.valor || this.valor <= 0 || this.numeroConta == null) {
      this.mensagem = 'Valor de depósito ou número da conta inválidos.';
      this.limparMensagem();
      return;
    }

    // Chamada ao serviço
    this.contaService.postDeposit(this.valor, this.numeroConta).subscribe({
      next: (res) => {
        this.mensagem = `Depósito de R$ ${this.valor} na conta ${this.numeroConta} realizado com sucesso!`;
        this.limparMensagem();
        this.valor = null;
        this.numeroConta = null;
      },

      error: (err) => {
        this.mensagem = err.error?.message || 'Erro ao processar o depósito. Verifique os dados e tente novamente.';
        this.limparMensagem();
      }
    });
  }

  private limparMensagem() {
    setTimeout(() => {
      this.mensagem = '';
    }, 3000);
  }
}
