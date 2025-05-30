import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha

@Component({
  selector: 'app-depositar',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './depositar.html',
  styleUrl: './depositar.css'
})
export class Depositar {
  numeroConta: number = 0;
  valor: number = 0;
  mensagem: string = '';


  depositar() {
    if (this.valor > 0 && this.numeroConta) {
      console.log("--deu certo --")
      this.mensagem = `Depósito de R$ ${this.valor} na conta ${this.numeroConta} realizado com sucesso!`;
      this.valor = 0;
      this.numeroConta = 0;
    } else {
      this.mensagem = 'Informe um número de conta e um valor válido para depósito.';
    }
    setTimeout(() => {
        this.mensagem = '';
      }, 3000);
  }
}
