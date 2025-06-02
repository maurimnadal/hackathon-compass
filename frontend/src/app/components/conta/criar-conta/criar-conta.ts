import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-criar-conta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './criar-conta.html',
  styleUrls: ['./criar-conta.css']
})
export class CriarContaComponent {
  clienteId: string = '';
  tipoConta: string = '';
  mensagem: string = '';

  onSubmit(): void {
    if (!this.clienteId || !this.tipoConta) return;

    this.mensagem = `Conta do tipo '${this.tipoConta}' criada com sucesso para o cliente ${this.clienteId}.`;
    this.clienteId = '';
    this.tipoConta = '';
  }
}
