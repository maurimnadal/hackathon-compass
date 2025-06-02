import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ContaService } from '../../../services/conta/conta.service';
import { Conta } from '../../../models/conta/conta';

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
  numeroConta: number = 0;
  valor: number = 0;
  mensagem: string = '';
  saldo: number = 0;
  tipo: string = '';
  resposta: string = '';

  verificarNumeroConta(): boolean {
    return this.numeroConta != null && this.numeroConta > 0;
  }

  constructor(private contaService: ContaService) { }


  depositar() {
    // Validação do valor e número da conta
    if (!this.valor || this.valor <= 0) {
      this.mensagem = 'Informe um valor válido para depósito.';
      this.resposta = 'erro';
      this.limparMensagem();
      return;
    }

    if (!this.verificarNumeroConta() || this.numeroConta <= 0) {
      this.mensagem = 'Informe um número de conta válido.';
      this.resposta = 'erro';
      this.limparMensagem();
      return;
    }

    // Chamada ao serviço
    this.contaService.postDeposit(this.valor, this.numeroConta).subscribe({
      next: (res) => {
        this.mensagem = `Depósito de R$ ${this.valor} na conta ${this.numeroConta} realizado com sucesso!`;
        this.resposta = 'sucesso';
        this.valor = 0;
        this.numeroConta = 0;
      },

      error: (err) => {
        this.mensagem = err.error?.message || 'Erro ao processar o depósito. Verifique os dados e tente novamente.';
        this.resposta = 'erro';
      }
    });

    this.limparMensagem();
  }

  private limparMensagem() {
    setTimeout(() => {
      this.mensagem = '';
      this.resposta = '';
    }, 3000);
  }

}
