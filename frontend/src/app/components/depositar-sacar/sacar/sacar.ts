import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ContaService } from '../../../services/conta/conta.service';


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
  valor: number | null = null;
  mensagem: string = '';

  constructor(private contaService: ContaService) { }


  sacar() {
    if (!this.valor || this.valor <= 0 || this.numeroConta == null) {
      this.mensagem = 'Valor de saque ou número da conta inválidos.';
      this.limparMensagem();
      return;
    }

    this.contaService.postWithdraw(this.valor, this.numeroConta).subscribe({
      next: (res) => {
        this.mensagem = `Saque de R$ ${this.valor} na conta ${this.numeroConta} realizado com sucesso!`;
        this.valor = null;
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
