import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Adicione esta linha
import { CommonModule } from '@angular/common'; // Adicione esta linha
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

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
  numeroConta: number = 0;
  valor: number = 0;
  mensagem: string = '';


  verificarNumeroConta() {
    if (this.numeroConta <=0) {
      return false;
    }
    return true;
  }
  verificarValor() {
    if (this.valor <=0) {
      return false;
    }
    return true;
  }


  sacar() {
    if (this.verificarValor() && this.verificarNumeroConta()) {
      this.mensagem = `Saque de R$ ${this.valor} na conta ${this.numeroConta} realizado com sucesso!`;
      this.valor = 0;
      this.numeroConta = 0;
    } else {
      this.mensagem = 'Informe um número de conta e um valor válido para saque.';
    }
    setTimeout(() => {
      this.mensagem = '';
    }, 3000);
  }
}
