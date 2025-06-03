import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AccountService } from '../../../services/account/account.service';
import { Account } from '../../../models/account/account';

@Component({
  selector: 'app-criar-conta',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './criar-conta.html',
  styleUrls: ['./criar-conta.css']
})
export class CriarContaComponent {
  customer_id: number | null = null;
  type: string = '';
  mensagem: string = '';
  isLoading: boolean = false;
  isSuccess: boolean = false;
  isError: boolean = false;

  constructor(private contaService: AccountService) { }

  onSubmit(): void {
    if (this.customer_id === null || !this.type) {
      this.mensagem = 'Preencha todos os campos.';
      this.isError = true;
      this.isSuccess = false;
      setTimeout(() => {
        this.isError = false;
        this.mensagem = '';
      }, 3000);
      return;
    }

    this.isLoading = true;

    // Criando objeto de conta conforme a interface
    const account: Omit<Account, "id" | "balance">= {
      accountType: this.type,
    };

    // Adicionando customerId como parâmetro separado ou no corpo da requisição
    this.contaService.postAccount(this.customer_id, account).subscribe({
      next: (response) => {
        this.mensagem = `Conta criada com sucesso para cliente ID ${this.customer_id} do tipo ${this.type}!`;
        this.isSuccess = true;
        this.isError = false;
        this.customer_id = null;
        this.type = '';
        this.isLoading = false;
      },
      error: (err) => {
        this.mensagem = err.error?.message || 'Erro ao criar conta. Verifique os dados e tente novamente.';
        this.isError = true;
        this.isSuccess = false;
        this.isLoading = false;
      }
    });
  }
}
