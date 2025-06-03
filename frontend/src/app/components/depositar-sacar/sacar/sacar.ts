import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { AccountService } from '../../../services/account/account.service';

@Component({
  selector: 'app-sacar',
  imports: [
    FormsModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './sacar.html',
  styleUrl: './sacar.css'
})

export class Sacar {
  numeroConta: number | null = null;
  amount: number | null = null;
  mensagem: string = '';

  constructor(private accountService: AccountService) { }


  sacar() {
    if (!this.amount || this.amount <= 0 || this.numeroConta == null) {
      this.mensagem = 'amount de saque ou número da conta inválidos.';
      this.limparMensagem();
      return;
    }

    this.accountService.postWithdraw(this.amount, this.numeroConta).subscribe({
      next: (res) => {
        this.mensagem = `Saque de R$ ${this.amount} na conta ${this.numeroConta} realizado com sucesso!`;
        this.amount = null;
        this.numeroConta = null;
        this.limparMensagem();
      },
      error: (err) => {
        this.mensagem = err.error?.message || 'Erro ao processar o saque. Verifique os dados e tente novamente.';
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
