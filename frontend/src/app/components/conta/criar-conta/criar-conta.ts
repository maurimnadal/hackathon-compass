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
  clientes = [
    { id: 1, nome: 'Cliente Teste 1' },
    { id: 2, nome: 'Cliente Teste 2' }
  ];

  customer_id: number | null = null;
  type: string = '';
  mensagem: string = '';

  onSubmit(): void {
    if (this.customer_id === null || !this.type) {
      this.mensagem = 'Preencha todos os campos.';
      return;
    }

    // Simulação de "salvar" com delay
    setTimeout(() => {
      this.mensagem = `Conta criada com sucesso para cliente ID ${this.customer_id} do tipo ${this.type}!`;
      this.customer_id = null;
      this.type = '';
    }, 1000);
  }
}
